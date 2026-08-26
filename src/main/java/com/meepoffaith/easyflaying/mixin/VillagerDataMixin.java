package com.meepoffaith.easyflaying.mixin;

import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// There's probably a better way to do this...
@Mixin(VillagerData.class)
abstract class VillagerDataMixin{
    @Shadow @Final private CompoundTag nbt;

    @Inject(method = "of(Lnet/minecraft/world/entity/npc/Villager;)Lde/maxhenkel/easyvillagers/datacomponents/VillagerData;", at = @At("TAIL"), remap = false)
    private static void easyflaying$of(
            Villager villager,
            CallbackInfoReturnable<VillagerData> cir
    ){
        VillagerData data = cir.getReturnValue();
        CompoundTag nbt = ((VillagerDataAccessor)data).easyflaying$getnbt();
        CompoundTag persistent = villager.getPersistentData();
        if(!persistent.isEmpty()) {
            nbt.put("NeoForgeData", villager.getPersistentData().copy());
        }
    }

    @Inject(method = "createEasyVillager(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lde/maxhenkel/easyvillagers/entity/EasyVillagerEntity;", at = @At("TAIL"), remap = false)
    public void easyflaying$createEasyVillager(
            Level level,
            ItemStack stack,
            CallbackInfoReturnable<EasyVillagerEntity> cir
    ){
        if(nbt.contains("NeoForgeData")){
            CompoundTag data = nbt.getCompound("NeoForgeData");
            EasyVillagerEntity villager = cir.getReturnValue();
            for(String key : data.getAllKeys()){
                villager.getPersistentData().put(key, data.get(key));
            }
        }
    }
}
