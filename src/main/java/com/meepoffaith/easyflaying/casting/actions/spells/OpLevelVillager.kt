package com.meepoffaith.easyflaying.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import com.meepoffaith.easyflaying.mixin.VillagerAccessor
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTraderWithVillager
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.VillagerData

// From the Minecraft Wiki (https://minecraft.wiki/w/Villager#Experience_levels)
// +10 to Apprentice, +60 to Journeyman, +80 to Expert, +100 to Master
// Cost: 3 dust per xp?

object OpLevelVillager : SpellAction {
    const val BASE_COST = MediaConstants.DUST_UNIT * 3
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val trader = args.getAnyTraderWithVillager(env, true, 0)
        val villager = trader.villagerEntity!!
        val currentLevel = villager.villagerData.level
        val nextLevel = VillagerData.getMaxXpPerLevel(currentLevel)
        if(nextLevel == 0)
            throw MishapBadBlock.of(args.getBlockPos(0), "easyflaying:trader.any.maxed")
        val difference = nextLevel - villager.villagerXp

        return SpellAction.Result(
            Spell(villager, nextLevel),
            BASE_COST * difference,
            listOf(ParticleSpray.cloud(trader.blockPos.center, 1.0, 10))
        )
    }

    private data class Spell(val villager: Villager, val xp: Int) : RenderedSpell{
        override fun cast(env: CastingEnvironment) {
            villager.villagerXp = xp
            (villager as VillagerAccessor).`easyflaying$levelUp`()
        }
    }
}
