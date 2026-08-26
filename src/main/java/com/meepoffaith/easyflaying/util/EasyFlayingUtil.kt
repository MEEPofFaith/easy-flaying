package com.meepoffaith.easyflaying.util

import at.petrak.hexcasting.api.HexAPI
import com.meepoffaith.easyflaying.mixin.VillagerDataAccessor
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity
import net.minecraft.world.item.ItemStack

object EasyFlayingUtil{
    fun brainsweep(trader: TraderTileentityBase, sacrifice: EasyVillagerEntity){
        HexAPI.instance().brainsweep(sacrifice)
        VillagerData.applyToItem(trader.villager, sacrifice)

        /*
            TODO: Do I need to add networking stuff?
                  I'm not experienced enough to know when I do and don't need it.
                  I see it in base hex brainsweep code, at least.
         */
    }

    fun isBrainswept(stack: ItemStack): Boolean{
        val data = VillagerData.get(stack) ?: return false

        val nbt = (data as VillagerDataAccessor).`easyflaying$getnbt`()
        return nbt.getCompound("NeoForgeData").getBoolean("hexcasting:brainswept")
    }
}
