package com.meepoffaith.easyflaying.mixin;

import com.meepoffaith.easyflaying.util.EasyFlayingUtil;
import de.maxhenkel.easyvillagers.blocks.FarmerBlock;
import de.maxhenkel.easyvillagers.blocks.VillagerBlockBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmerBlock.class)
abstract class FarmerBlockMixin{
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lde/maxhenkel/easyvillagers/blocks/tileentity/FarmerTileentity;setVillager(Lnet/minecraft/world/item/ItemStack;)V"), cancellable = true, remap = false)
    protected void easyflaying$useItemOn(
            ItemStack heldItem,
            BlockState state,
            Level worldIn,
            BlockPos pos,
            Player player,
            InteractionHand handIn,
            BlockHitResult hit,
            CallbackInfoReturnable<ItemInteractionResult> cir
    ){
        if(EasyFlayingUtil.INSTANCE.isBrainswept(heldItem)){
            VillagerBlockBase.playVillagerSound(worldIn, pos, SoundEvents.VILLAGER_HURT);
            player.displayClientMessage(Component.translatable("easyflaying.info.brainswept").withStyle(ChatFormatting.DARK_PURPLE), true);
            cir.setReturnValue(ItemInteractionResult.CONSUME);
        }
    }
}
