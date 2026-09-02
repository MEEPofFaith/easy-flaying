package com.meepoffaith.easyflaying.casting.actions.getters

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAutoTrader

object OpGetAutotraderIndex : ConstMediaAction{
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val target = args.getAutoTrader(env.world, 0)
        return (if(target.hasVillager()) target.tradeIndex else -1).asActionResult
    }
}
