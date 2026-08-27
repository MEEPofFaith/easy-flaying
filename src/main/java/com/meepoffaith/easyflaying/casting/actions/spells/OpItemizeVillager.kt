package com.meepoffaith.easyflaying.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getVillager
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import de.maxhenkel.easyvillagers.items.ModItems
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.item.ItemStack

object OpItemizeVillager : SpellAction{
    const val COST = MediaConstants.SHARD_UNIT
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = args.getVillager(env.world, 0)

        return target.map({ item ->
            SpellAction.Result(
                SpellItem(item),
                COST,
                listOf(ParticleSpray.burst(item.position(), 2.0))
            )
        }, { villager ->
            SpellAction.Result(
                SpellVillager(villager),
                COST,
                listOf(ParticleSpray.burst(villager.position(), 1.0))
            )
        })
    }

    private data class SpellItem(val target: ItemEntity) : RenderedSpell{
        override fun cast(env: CastingEnvironment) {
            val villager = VillagerData.getOrCreate(target.item).createEasyVillager(env.world, target.item)
            villager.setPos(target.position())
            if(env.world.addFreshEntity(villager)){
                target.discard()
            }
        }
    }

    private data class SpellVillager(val target: Villager) : RenderedSpell{
        override fun cast(env: CastingEnvironment) {
            val stack = ItemStack(ModItems.VILLAGER.get())
            VillagerData.applyToItem(stack, target)
            val pos = target.position()
            val item = ItemEntity(env.world, pos.x, pos.y, pos.z, stack)
            if(env.world.addFreshEntity(item)) {
                target.discard()
            }
        }
    }
}
