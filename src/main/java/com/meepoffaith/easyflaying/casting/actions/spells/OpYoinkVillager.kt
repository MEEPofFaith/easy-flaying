package com.meepoffaith.easyflaying.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem
import at.petrak.hexcasting.api.misc.MediaConstants
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import de.maxhenkel.easyvillagers.items.ModItems
import de.maxhenkel.easyvillagers.items.VillagerItem
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.item.ItemStack

object OpYoinkVillager : SpellAction{
    const val COST = MediaConstants.SHARD_UNIT
    override val argc = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result{
        val target = args.getEntity(env.world, 0)
        val trader = args.getBlockPos(1)
        val tile = env.world.getBlockEntity(trader)

        if(tile !is TraderTileentityBase || tile.hasVillager())
            throw MishapBadBlock.of(trader, "easyflaying:empty_trader")

        if(target is Villager && target.isAlive){
            return SpellAction.Result(
                SpellVillager(target, tile),
                COST,
                listOf(ParticleSpray.cloud(target.eyePosition, 1.0), ParticleSpray.burst(trader.center, 1.0))
            )
        }else if(target is ItemEntity){
            if(target.item.item !is VillagerItem)
                throw MishapBadItem.of(target, "easyflaying:villager")
            return SpellAction.Result(
                SpellStack(target, tile),
                COST,
                listOf(ParticleSpray.cloud(target.eyePosition, 0.5), ParticleSpray.burst(trader.center, 1.0))
            )
        }
        throw MishapBadEntity.of(target, "easyflaying:villager")
    }

    private data class SpellVillager(val target: Villager, val trader: TraderTileentityBase) : RenderedSpell {
        override fun cast(env: CastingEnvironment){
            val stack = ItemStack(ModItems.VILLAGER.get())
            VillagerData.applyToItem(stack, target)
            trader.villager = stack
            target.discard()
        }
    }

    private data class SpellStack(val target: ItemEntity, val trader: TraderTileentityBase) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            trader.villager = target.item
            target.discard()
        }
    }
}
