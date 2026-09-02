package com.meepoffaith.easyflaying.casting.actions.getters

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTrader

object OpGetTradesCount : ConstMediaAction{
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val trader = args.getAnyTrader(env.world, 0)
        if(!trader.hasVillager()) return (-1).asActionResult

        return trader.villagerEntity!!.offers.size.asActionResult
    }
}
