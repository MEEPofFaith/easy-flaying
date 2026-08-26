package com.meepoffaith.easyflaying.datagen

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.common.lib.HexRegistries
import com.meepoffaith.easyflaying.EasyFlaying
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import java.util.concurrent.CompletableFuture

class EasyFlayingActionTags(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
) : TagsProvider<ActionRegistryEntry>(output, HexRegistries.ACTION, provider, EasyFlaying.MODID, null) {
    override fun addTags(provider: HolderLookup.Provider) {
        // per-world great spells
        // Not likely to need, but I'll keep this here just in case.
        /*
        for (entry in arrayOf(
            HextrapatsActions.GREAT_CONGRATULATE,
        )) {
            tag(HexTags.Actions.CAN_START_ENLIGHTEN).add(entry.key)
            tag(HexTags.Actions.PER_WORLD_PATTERN).add(entry.key)
            tag(HexTags.Actions.REQUIRES_ENLIGHTENMENT).add(entry.key)
        }
         */
    }
}
