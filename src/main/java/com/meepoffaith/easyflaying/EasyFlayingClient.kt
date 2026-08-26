package com.meepoffaith.easyflaying

import at.petrak.hexcasting.interop.HexInterop
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import vazkii.patchouli.api.PatchouliAPI

// https://github.com/MEEPofFaith/hextra-patterns-1.21/blob/main/src/neoforge/1.21.1/main/kotlin/com/meepoffaith/hextrapats/client/NeoForgeHextrapatsClient.kt
object EasyFlayingClient{
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLClientSetupEvent){
        PatchouliAPI.get().setConfigFlag(HexInterop.PATCHOULI_ANY_INTEROP_FLAG, true)
    }
}
