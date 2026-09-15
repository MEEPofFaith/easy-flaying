package com.meepoffaith.easyflaying.casting.actions.getters

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTraderWithVillager
import ram.talia.moreiotas.api.casting.iota.ItemStackIota

object OpGetTrades : ConstMediaAction{
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val trader = args.getAnyTraderWithVillager(env.world, true, 0)
        val offers = trader.villagerEntity!!.offers
        val trades = mutableListOf<Iota>()
        for(offer in offers){
            val trade = mutableListOf<Iota>()
            val cost = mutableListOf<Iota>(ItemStackIota.createFiltered(offer.costA))
            val costB = offer.costB
            if(!costB.isEmpty){
                cost.add(ItemStackIota.createFiltered(costB))
            }
            trade.add(ListIota(cost))
            trade.add(ItemStackIota.createFiltered(offer.result))
            trades.add(ListIota(trade))
        }
        return trades.asActionResult
    }
}
