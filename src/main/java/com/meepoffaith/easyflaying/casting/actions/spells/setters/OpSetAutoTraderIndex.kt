package com.meepoffaith.easyflaying.casting.actions.spells.setters

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAutoTraderWithVillager
import de.maxhenkel.easyvillagers.blocks.tileentity.AutoTraderTileentity

object OpSetAutoTraderIndex : SpellAction {
    const val COST = MediaConstants.DUST_UNIT / 100
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val trader = args.getAutoTraderWithVillager(env.world, true, 0)
        val index = args.getPositiveIntUnder(1, trader.villagerEntity!!.offers.size)

        return SpellAction.Result(
            Spell(trader, index),
            COST,
            listOf()
        )
    }

    private data class Spell(val trader: AutoTraderTileentity, val index: Int) : RenderedSpell{
        override fun cast(env: CastingEnvironment){
            trader.tradeIndex = index
        }
    }
}
