package com.meepoffaith.easyflaying.mixin;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBrainsweep;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.common.casting.actions.spells.great.OpBrainsweep;
import at.petrak.hexcasting.common.recipe.BrainsweepRecipe;
import at.petrak.hexcasting.common.recipe.HexRecipeStuffRegistry;
import at.petrak.hexcasting.mixin.accessor.AccessorLivingEntity;
import com.meepoffaith.easyflaying.casting.mishaps.MishapAlreadyBrainsweptTrader;
import com.meepoffaith.easyflaying.util.EasyFlayingUtil;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// Loosely based upon
// https://github.com/withgallantry/HexIntent/blob/main/src/main/java/com/bluup/manifestation/mixin/OpBrainsweepMixin.java
// because this is my first time doing something like this.
@Mixin(OpBrainsweep.class)
abstract class OpBrainsweepMixin{
    @Shadow @Final private static int argc;

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void easyflaying$flayTraderBlock(
        List<Iota> args,
        CastingEnvironment env,
        CallbackInfoReturnable<SpellAction.Result> cir
    ){
        Iota arg0 = args.getFirst();
        if(!(arg0 instanceof Vec3Iota traderVec)) return; // Go back to original flay mind
        Vec3 targetVec = OperatorUtils.getVec3(args, 1, argc);

        BlockPos traderPos = BlockPos.containing(traderVec.getVec3());
        BlockPos targetPos = BlockPos.containing(targetVec);

        if(!env.canEditBlockAt(targetPos))
            throw new MishapBadLocation(targetVec, "forbidden");

        env.assertPosInRange(traderPos);
        env.assertPosInRange(targetPos);

        var world = env.getWorld();

        BlockEntity traderEntity = world.getBlockEntity(traderPos);

        if(!(traderEntity instanceof TraderTileentityBase traderBlock))
            throw MishapBadBlock.of(traderPos, "easyflaying:filled_trader");

        var sacrifice = traderBlock.getVillagerEntity();
        if(sacrifice == null)
            throw MishapBadBlock.of(traderPos, "easyflaying:filled_trader");

        // Flay mind expects an in-world mob, so I can't just convert to an entity iota and pass it in.
        // Manually re-implement with proper handling of the trader.
        if(sacrifice.getType().is(HexTags.Entities.NO_BRAINSWEEPING))
            throw new MishapBadBrainsweep(sacrifice, targetPos);

        if(HexAPI.instance().isBrainswept(sacrifice))
            throw new MishapAlreadyBrainsweptTrader(traderBlock, sacrifice);

        var state = env.getWorld().getBlockState(targetPos);

        var recman = env.getWorld().getRecipeManager();
        var recipes = recman.getAllRecipesFor(HexRecipeStuffRegistry.BRAINSWEEP_TYPE.get());
        var recipeQuestionMark = recipes.stream() // Kotlinless moment
                .map(RecipeHolder::value)
                .filter(it -> it.matches(state, sacrifice, env.getWorld()))
                .findFirst();

        if(recipeQuestionMark.isEmpty())
            throw new MishapBadBrainsweep(sacrifice, targetPos);
        var recipe = recipeQuestionMark.get();

        SpellAction.Result result = new SpellAction.Result(
                new Spell(targetPos, state, traderBlock, sacrifice, recipe),
                recipe.mediaCost(),
                List.of(ParticleSpray.cloud(traderPos.getCenter(), 2.0, 40), ParticleSpray.burst(targetPos.getCenter(), 0.3, 100)),
                1
            );

        cir.setReturnValue(result);
    }

    // Re-implement the flaying spell to handle a villager inside a trader
    private record Spell(
            BlockPos pos,
            BlockState state,
            TraderTileentityBase trader,
            EasyVillagerEntity sacrifice,
            BrainsweepRecipe recipe
    ) implements RenderedSpell{
        @Override
        public void cast(@NotNull CastingEnvironment env) {
            env.getWorld().setBlockAndUpdate(pos, BrainsweepRecipe.copyProperties(state, recipe.result()));

            EasyFlayingUtil.INSTANCE.brainsweep(trader, sacrifice);

            var sound = ((AccessorLivingEntity)sacrifice).hex$getDeathSound();
            if(sound != null)
                env.getWorld().playSound(null, trader.getBlockPos(), sound, SoundSource.AMBIENT, 0.8f, 1f);
            env.getWorld().playSound(null, trader.getBlockPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 0.5f, 0.8f);
        }
    }
}
