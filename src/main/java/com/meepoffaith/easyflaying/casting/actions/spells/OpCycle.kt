package com.meepoffaith.easyflaying.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import com.meepoffaith.easyflaying.util.EasyFlayingImageData
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTraderWithVillager
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.npc.Villager
import kotlin.math.pow
import kotlin.math.roundToLong

object OpCycle : SpellAction{
    val COST = MediaConstants.DUST_UNIT / 2
    override val argc = 1

    override fun executeWithUserdata(
        args: List<Iota>,
        env: CastingEnvironment,
        userData: CompoundTag
    ) : SpellAction.Result {
        val trader = args.getAnyTraderWithVillager(env, true, 0)
        val villager = trader.villagerEntity!!
        if(villager.villagerXp > 0)
            throw MishapBadBlock.of(args.getBlockPos(0), "easyflaying:trader.any.leveled")

        val multiplier = 1.5.pow(EasyFlayingImageData.checkAndCountRefreshed(userData, villager))

        return SpellAction.Result(
            Spell(villager),
            (COST * multiplier).roundToLong(),
            listOf(ParticleSpray.cloud(trader.blockPos.center, 1.0, 10))
        )
    }

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        throw IllegalStateException()
    }

    private data class Spell(val villager: Villager) : RenderedSpell{
        override fun cast(env: CastingEnvironment) {
            villager.offers = null
            EasyVillagerEntity.recalculateOffers(villager)
        }
    }
}
