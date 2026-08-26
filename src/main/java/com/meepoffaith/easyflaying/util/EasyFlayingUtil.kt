package com.meepoffaith.easyflaying.util

import at.petrak.hexcasting.api.HexAPI
import com.meepoffaith.easyflaying.EasyFlaying
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity

object EasyFlayingUtil{
    /*
        TODO: Maybe try to actually make a brainswept villager instead of Thanos snapping them from existence.
              Just calling HexAPI.instance().brainsweep doesn't seem to cause any effect to occur.
     */
    fun brainsweep(trader: TraderTileentity, sacrifice: EasyVillagerEntity){
        //trader.removeVillager()
        HexAPI.instance().brainsweep(sacrifice)
        VillagerData.applyToItem(trader.villager, sacrifice)

        /*
            TODO: Do I need to add networking stuff?
                  I'm not experienced enough to know when I do and don't need it.
                  I see it in base hex brainsweep code, at least.
         */
    }
}
