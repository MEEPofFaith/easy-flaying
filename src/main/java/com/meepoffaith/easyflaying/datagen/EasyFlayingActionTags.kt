package com.meepoffaith.easyflaying.datagen

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.lib.HexRegistries
import com.meepoffaith.easyflaying.EasyFlaying
import com.meepoffaith.easyflaying.init.EasyFlayingActions
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import java.util.concurrent.CompletableFuture

// https://github.com/MEEPofFaith/hextra-patterns-1.21/blob/main/src/common/main/kotlin/com/meepoffaith/hextrapats/datagen/HextrapatsActionTags.kt
class EasyFlayingActionTags(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
) : TagsProvider<ActionRegistryEntry>(output, HexRegistries.ACTION, provider, EasyFlaying.MODID, null) {
    override fun addTags(provider: HolderLookup.Provider) {
        for (entry in arrayOf(
            EasyFlayingActions.YOINK_VILLAGER
        )){
            tag(HexTags.Actions.CAN_START_ENLIGHTEN).add(entry.key)
            tag(HexTags.Actions.PER_WORLD_PATTERN).add(entry.key)
            tag(HexTags.Actions.REQUIRES_ENLIGHTENMENT).add(entry.key)
        }
    }
}
