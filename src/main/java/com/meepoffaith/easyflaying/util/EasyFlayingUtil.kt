package com.meepoffaith.easyflaying.util

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import com.meepoffaith.easyflaying.mixin.VillagerDataAccessor
import com.mojang.datafixers.util.Either
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase
import de.maxhenkel.easyvillagers.datacomponents.VillagerData
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity
import de.maxhenkel.easyvillagers.items.VillagerItem
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.item.ItemStack

object EasyFlayingUtil{
    fun brainsweep(trader: TraderTileentityBase, sacrifice: EasyVillagerEntity){
        HexAPI.instance().brainsweep(sacrifice)
        VillagerData.applyToItem(trader.villager, sacrifice)
    }

    fun ItemStack.isBrainswept(): Boolean{
        val data = VillagerData.get(this) ?: return false

        val nbt = (data as VillagerDataAccessor).`easyflaying$getnbt`()
        return nbt.getCompound("NeoForgeData").getBoolean("hexcasting:brainswept")
    }

    fun List<Iota>.getTrader(level: ServerLevel, isFull: Boolean, idx: Int, argc: Int = 0): TraderTileentityBase {
        val pos = this.getBlockPos(idx, argc)
        val trader = level.getBlockEntity(pos)
        if(trader !is TraderTileentityBase || trader.hasVillager() != isFull)
            throw MishapBadBlock.of(pos, "easyflaying:" + (if (isFull) "filled" else "empty") + "_trader")
        return trader
    }

    fun List<Iota>.getVillager(level: ServerLevel, idx: Int, argc: Int = 0): Either<Villager, ItemEntity> {
        val datum = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if(datum is EntityIota) {
            val entity = datum.getEntity(level)
            when(entity){
                is Villager if entity.isAlive ->
                    return Either.left(entity)
                is ItemEntity if entity.item.item is VillagerItem ->
                    return Either.right(entity)
            }
        }
        throw MishapInvalidIota.of(
            datum,
            if (argc == 0) idx else argc - (idx + 1),
            "easyflaying:villager"
        )
    }
}
