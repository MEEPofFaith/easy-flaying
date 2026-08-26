package com.meepoffaith.easyflaying.mixin;

import de.maxhenkel.easyvillagers.datacomponents.VillagerData;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
        VillagerData data = VillagerData.get(stack);
        if(data == null) return;

        CompoundTag nbt = ((VillagerDataAccessor)data).easyflaying$getnbt();
        if(nbt.contains("NeoForgeData")){
            CompoundTag persistent = nbt.getCompound("NeoForgeData");
            if(persistent.contains("hexcasting:brainswept")){
                tooltip.add(Component.translatable("easyflaying.tooltip.flain").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }
}
