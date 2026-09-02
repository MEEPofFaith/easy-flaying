package com.meepoffaith.easyflaying.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.meepoffaith.easyflaying.util.EasyFlayingUtil.getAnyTraderWithVillager
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.phys.Vec3

class OpExpelVillager(val toItem: Boolean) : SpellAction{
    val COST = MediaConstants.SHARD_UNIT
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val trader = args.getAnyTraderWithVillager(env.world, true, 0)
        val target = args.getVec3(1)

        return SpellAction.Result(
            Spell(trader, target, toItem),
            COST,
            listOf(ParticleSpray.cloud(trader.blockPos.center, 1.0), ParticleSpray.burst(target, 1.0, 40))
        )
    }

    private data class Spell(val trader: TraderTileentityBase, val pos: Vec3, val toItem: Boolean) : RenderedSpell {
        override fun cast(env: CastingEnvironment){
            val stack = trader.villager
            var entity: Entity
            if(toItem){
                entity = ItemEntity(env.world, pos.x, pos.y, pos.z, stack)
            }else{
                entity = VillagerData.getOrCreate(stack).createEasyVillager(env.world, stack)
                entity.setPos(pos)
            }
            if(env.world.addFreshEntity(entity)){
                trader.removeVillager()
            }
        }
    }
}
