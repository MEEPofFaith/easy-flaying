package com.meepoffaith.easyflaying.util

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
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

        /*
            TODO: Do I need to add networking stuff?
                  I'm not experienced enough to know when I do and don't need it.
                  I see it in base hex brainsweep code, at least.
         */
    }

    fun isBrainswept(stack: ItemStack): Boolean{
        val data = VillagerData.get(stack) ?: return false

        val nbt = (data as VillagerDataAccessor).`easyflaying$getnbt`()
        return nbt.getCompound("NeoForgeData").getBoolean("hexcasting:brainswept")
    }

    fun List<Iota>.getVillager(level: ServerLevel, idx: Int, argc: Int = 0): Either<ItemEntity, Villager> {
        val datum = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if(datum is EntityIota) {
            val entity = datum.getEntity(level)
            return when(entity) {
                is ItemEntity if entity.item.item is VillagerItem ->
                    Either.left(entity)
                is Villager if entity.isAlive ->
                    Either.right(entity)
                else -> throw MishapInvalidIota.of(
                    datum,
                    if (argc == 0) idx else argc - (idx + 1),
                    "easyflaying:villager"
                )
            }
        }
        throw MishapInvalidIota.of(
            datum,
            if (argc == 0) idx else argc - (idx + 1),
            "easyflaying:villager"
        )
    }
}
