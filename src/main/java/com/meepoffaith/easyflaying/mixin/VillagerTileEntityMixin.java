package com.meepoffaith.easyflaying.mixin;

import de.maxhenkel.easyvillagers.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerTileentity.class)
abstract class VillagerTileEntityMixin{
    @Shadow protected EasyVillagerEntity villagerEntity;
}
