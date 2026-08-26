package com.meepoffaith.easyflaying.datagen

import at.petrak.hexcasting.forge.datagen.TagsProviderEFHSetter
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.data.event.GatherDataEvent

// https://github.com/MEEPofFaith/hextra-patterns-1.21/blob/main/src/neoforge/1.21.1/main/kotlin/com/meepoffaith/hextrapats/datagen/NeoForgeHextrapatsDatagen.kt
object EasyFlayingDatagen{
    fun init(event: GatherDataEvent) {
        event.apply {
            addVanillaProvider(includeServer()) { EasyFlayingActionTags(it, lookupProvider) }
        }
    }
}

private fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (PackOutput) -> T) =
    generator.addProvider(run) { factory(it) }

private fun <T : DataProvider> GatherDataEvent.addVanillaProvider(run: Boolean, factory: (PackOutput) -> T) =
    addProvider(run) { packOutput ->
        factory(packOutput).also {
            (it as TagsProviderEFHSetter).setEFH(existingFileHelper)
        }
    }
