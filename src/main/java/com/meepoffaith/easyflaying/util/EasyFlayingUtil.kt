package com.meepoffaith.easyflaying.util

import at.petrak.hexcasting.api.HexAPI
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity

object EasyFlayingUtil{
    fun brainsweep(trader: TraderTileentity, sacrifice: EasyVillagerEntity){
        HexAPI.instance().brainsweep(sacrifice)
        VillagerData.applyToItem(trader.villager, sacrifice)

        /*
            TODO: Do I need to add networking stuff?
                  I'm not experienced enough to know when I do and don't need it.
                  I see it in base hex brainsweep code, at least.
         */
    }
}
