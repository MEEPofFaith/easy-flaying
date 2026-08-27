package com.meepoffaith.easyflaying.init

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import com.meepoffaith.easyflaying.casting.actions.spells.OpItemizeVillager
import com.meepoffaith.easyflaying.casting.actions.spells.OpYoinkVillager

object EasyFlayingActions : EasyFlayingRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY }
) {
    val ITEMIZE_VILLAGER = make("villager/itemize", HexDir.EAST, "qwqwqwqwqaeqedeqeaqadqdqdwewd", OpItemizeVillager)
    val YOINK_VILLAGER = make("villager/yoink", HexDir.NORTH_WEST, "eqwqwqwqaeqedeqeaqadqdeeaqqwwqwwqwwqwwqwwqw", OpYoinkVillager)

    private fun make(name: String, startDir: HexDir, signature: String, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) = register(name) {
        ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
    }
}
