package com.meepoffaith.easyflaying.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.meepoffaith.easyflaying.mixin.TraderTileentityBaseAccessor
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTraderWithVillager
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase

object OpRestock : SpellAction {
    const val COST = MediaConstants.CRYSTAL_UNIT * 5
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val trader = args.getAnyTraderWithVillager(env, true, 0)

        return SpellAction.Result(
            SpellRestock(trader),
            COST,
            listOf(ParticleSpray.cloud(trader.blockPos.center, 1.0, 80))
        )
    }

    private data class SpellRestock(val trader: TraderTileentityBase) : RenderedSpell{
        override fun cast(env: CastingEnvironment) {
            (trader as TraderTileentityBaseAccessor).`easyflaying$restock`()
        }
    }
}
