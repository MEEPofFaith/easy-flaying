package com.meepoffaith.easyflaying.mixin;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock;
import at.petrak.hexcasting.common.casting.actions.spells.great.OpBrainsweep;
import com.meepoffaith.easyflaying.EasyFlaying;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
abstract class OpBrainsweepMixin {
    @Shadow @Final private static int argc;

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void easyflaying$flayTraderBlock(
        List<Iota> args,
        CastingEnvironment env,
        CallbackInfoReturnable<SpellAction.Result> cir
    ){
        Vec3 traderVec = OperatorUtils.getVec3(args, 0, argc); // Mmm yes the Java experience
        Vec3 targetVec = OperatorUtils.getVec3(args, 1, argc);
        BlockPos traderPos = BlockPos.containing(traderVec);
        BlockPos targetPos = BlockPos.containing(targetVec);

        env.assertPosInRange(traderPos);
        env.assertPosInRange(targetPos);

        EasyFlaying.LOGGER.info("Hello, is this working? Pos1: " + traderPos + " | Pos2: " + targetPos);

        var world = env.getWorld();
        BlockState trader = world.getBlockState(traderPos);
        if(trader.getBlock() == ModBlocks.TRADER.get()){
            EasyFlaying.LOGGER.info("Trader found!");
        }else{
            throw MishapBadBlock.of(traderPos, "easyflaying:trader");
        }
    }
}
