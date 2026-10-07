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
import net.minecraft.world.entity.npc.VillagerProfession

// From the Minecraft Wiki (https://minecraft.wiki/w/Villager#Experience_levels)
// +10 to Apprentice, +60 to Journeyman, +80 to Expert, +100 to Master
// Cost: 20 dust * current level

object OpLevelVillager : SpellAction {
    const val BASE_COST = MediaConstants.DUST_UNIT * 20
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val trader = args.getAnyTraderWithVillager(env, true, 0)
        val villager = trader.villagerEntity!!
        val profession = villager.villagerData.profession
        val currentLevel = villager.villagerData.level
        val nextXp = VillagerData.getMaxXpPerLevel(currentLevel)
        if(nextXp == 0 || profession == VillagerProfession.NONE || profession == VillagerProfession.NITWIT)
            throw MishapBadBlock.of(args.getBlockPos(0), "easyflaying:trader.any.cannot_level")
        val minXp = VillagerData.getMinXpPerLevel(currentLevel)
        val progress = (villager.villagerXp - minXp).toDouble() / (nextXp - minXp)

        return SpellAction.Result(
            Spell(villager, nextXp),
            (BASE_COST * currentLevel * (1 - progress)).toLong(),
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
