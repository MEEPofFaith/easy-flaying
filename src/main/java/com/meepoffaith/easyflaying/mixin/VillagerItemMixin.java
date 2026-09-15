package com.meepoffaith.easyflaying.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.meepoffaith.easyflaying.util.EasyFlayingUtil;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VillagerItem.class)
abstract class VillagerItemMixin{
    @Inject(method = "appendHoverText", at = @At("TAIL"), remap = false)
    public void easyflaying$appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flagIn,
            CallbackInfo ci
    ){
        if(EasyFlayingUtil.INSTANCE.isBrainswept(stack))
            tooltip.add(Component.translatable("hexcasting.mishap.already_brainswept").withStyle(ChatFormatting.DARK_PURPLE));
    }

    @WrapWithCondition(
            method = "inventoryTick",
            at = @At(value = "INVOKE", target = "Lde/maxhenkel/easyvillagers/blocks/VillagerBlockBase;playRandomVillagerSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/sounds/SoundEvent;)V")
    )
    public boolean easyflaying$shutUp(Player player, SoundEvent soundEvent, ItemStack stack){
        return !EasyFlayingUtil.INSTANCE.isBrainswept(stack);
    }
}
