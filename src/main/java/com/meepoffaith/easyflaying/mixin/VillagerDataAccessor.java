package com.meepoffaith.easyflaying.mixin;

import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VillagerData.class)
public interface VillagerDataAccessor{
    @Accessor("nbt")
    CompoundTag easyflaying$getnbt();
}
