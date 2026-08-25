package com.meepoffaith.easyflaying.mixin;

import at.petrak.hexcasting.api.HexAPI;
import de.maxhenkel.easyvillagers.events.VillagerEvents;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: Perhaps I could instead figure out why putting a villager in a trader unflays them?
@Mixin(VillagerEvents.class)
abstract class VillagerEventsMixin{
    @Inject(method = "arePickupConditionsMet", at = @At("HEAD"), cancellable = true, remap = false)
    private static void easyflaying$arePickupConditionsMet(
            Villager villager,
            CallbackInfoReturnable<Boolean> cir
    ){
        if(HexAPI.instance().isBrainswept(villager))
            cir.setReturnValue(false);
    }
}
