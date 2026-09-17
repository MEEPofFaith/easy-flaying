package com.meepoffaith.easyflaying.mixin;

import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TraderTileentityBase.class)
public interface TraderTileentityBaseAccessor{
    @Invoker("restock")
    void easyflaying$restock();
}
