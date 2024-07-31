package com.brainsmash.broken_world.items.magical;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class CloakingCape extends TrinketItem {
    public CloakingCape(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        if (entity.getWorld() instanceof ServerWorld) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY));
        }
    }
}
