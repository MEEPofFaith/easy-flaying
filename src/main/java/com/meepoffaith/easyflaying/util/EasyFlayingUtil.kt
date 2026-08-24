package com.meepoffaith.easyflaying.util

import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentity

object EasyFlayingUtil{
    // TODO: Maybe try to actually make a brainswept villager instead of Thanos snapping them from existence
    fun brainsweep(trader: TraderTileentity){
        trader.removeVillager()

        // TODO: Do I need to add networking stuff? I'm not experienced to know when I do and don't need it. I see it in base hex brainsweep.
    }
}
