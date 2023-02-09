package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.items.armor.material.ArmorMaterialWithSetBonus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntityMixin {

    private boolean requireUpdate;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void updateState(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
        requireUpdate = true;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void checkSetBonus(CallbackInfo ci) {
        if (requireUpdate) {
            if (getArmorItems().iterator().next().getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getMaterial() instanceof ArmorMaterialWithSetBonus material) {
                    boolean flag = true;
                    for (ItemStack equipment : getArmorItems()) {
                        if (!(equipment.getItem() instanceof ArmorItem item && item.getMaterial().getClass().equals(
                                material.getClass()))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        material.processSetBonus(this);
                    }
                }
            }
            requireUpdate = false;
        }
    }
}
