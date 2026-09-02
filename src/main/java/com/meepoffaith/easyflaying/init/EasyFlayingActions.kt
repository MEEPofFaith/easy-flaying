package com.meepoffaith.easyflaying.init

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import com.meepoffaith.easyflaying.casting.actions.getters.OpGetAutotraderIndex
import com.meepoffaith.easyflaying.casting.actions.getters.OpGetTradesCount
import com.meepoffaith.easyflaying.casting.actions.getters.OpHasVillager
import com.meepoffaith.easyflaying.casting.actions.getters.OpRemainingTrades
import com.meepoffaith.easyflaying.casting.actions.spells.OpExpelVillager
import com.meepoffaith.easyflaying.casting.actions.spells.OpItemizeVillager
import com.meepoffaith.easyflaying.casting.actions.spells.OpYoinkVillager

object EasyFlayingActions : EasyFlayingRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY }
) {
    val HAS_TRADER = make("trader/has", HexDir.NORTH_EAST, "wqqwqwqqwaeaqawwdwewdw", OpHasVillager)
    val MAX_TRADER_INDEX = make("trader/index.amount", HexDir.NORTH_EAST, "qqwqwqqqwaqaw", OpGetTradesCount)
    val GET_AUTO_TRADER_INDEX = make("trader/index.get", HexDir.SOUTH_WEST, "wwaqeqwqqwqwqqqewqaww", OpGetAutotraderIndex)
    val REMAINING_TRADES = make("trader/remaining", HexDir.NORTH_EAST, "weqqqwqwqqaeeeq", OpRemainingTrades)

    val ITEMIZE_VILLAGER = make("villager/itemize", HexDir.EAST, "qwqwqwqwqaeqedeqeaqadqdqdwewd", OpItemizeVillager)
    val YOINK_VILLAGER = make("villager/yoink", HexDir.NORTH_WEST, "eqwqwqwqaeqedeqeaqadqdeeaqqwwqwwqwwqwwqwwqw", OpYoinkVillager)
    val EXPEL_AS_VILLAGER = make("villager/expel/villager", HexDir.SOUTH_WEST, "wawwqwwawdqwqwqwqaeqedeqeaqadqdweeedq", OpExpelVillager(false))
    val EXPEL_AS_ITEM = make("villager/expel/item", HexDir.SOUTH_WEST, "qqwqwqqdqwqwqwqaeqedeqeaqadqdweeedq", OpExpelVillager(true))

    private fun make(name: String, startDir: HexDir, signature: String, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) = register(name) {
        ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
    }
}
