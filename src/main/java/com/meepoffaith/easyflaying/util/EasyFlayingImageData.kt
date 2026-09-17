package com.meepoffaith.easyflaying.util

import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import com.meepoffaith.easyflaying.EasyFlaying
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.npc.Villager

object EasyFlayingImageData {
    val REFRESHED_USERDATA = EasyFlaying.id("refreshed").toString()

    fun checkAndCountRefreshed(userData: CompoundTag, villager: Villager): Int {
        val marked = userData.getOrCreateCompound(REFRESHED_USERDATA)
        val count = if(marked.contains(villager.stringUUID)){
            marked.getInt(villager.stringUUID)
        }else{
            0
        }
        marked.putInt(villager.stringUUID, count + 1)
        userData.putCompound(REFRESHED_USERDATA, marked)
        return count
    }
}
