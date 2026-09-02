package com.meepoffaith.easyflaying.casting.actions.getters

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTrader

object OpRemainingTrades : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val target = args.getAnyTrader(env.world, 0)
        if(!target.hasVillager()) return (-1).asActionResult

        val index = args.getInt(1)
        val offers = target.villagerEntity!!.offers

        if(index < 0 || index >= offers.size) return (-1).asActionResult
        val offer = offers[index]
        return (offer.maxUses - offer.uses).asActionResult
    }
}
