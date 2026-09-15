package com.meepoffaith.easyflaying.mixin;

import at.petrak.hexcasting.api.HexAPI;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TraderTileentityBase.class)
abstract class TraderTileentityBaseMixin extends VillagerTileEntityMixin{
    @WrapWithCondition(
            method = "tickServer",
            at = @At(value = "INVOKE", target = "Lde/maxhenkel/easyvillagers/blocks/VillagerBlockBase;playRandomVillagerSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;)V")
    )
    protected boolean easyflaying$shutUp(Level world, BlockPos pos, SoundEvent soundEvent){
        return !HexAPI.instance().isBrainswept(villagerEntity);
    }
}
