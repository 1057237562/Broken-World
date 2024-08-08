package com.brainsmash.broken_world.items.magical;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;

public class XPAmulet extends TrinketItem {
    public XPAmulet(Settings settings) {
        super(settings);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            Optional.ofNullable(stack.getNbt()).ifPresent(nbtCompound -> {
                if (player.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.getServer().execute(() -> player.addExperience(nbtCompound.getInt("xp")));
                }
            });
            stack.setNbt(new NbtCompound());
        }
        super.onEquip(stack, slot, entity);
    }

    @Override
    public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            player.disableExperienceDropping();
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("xp", player.totalExperience);
            stack.setNbt(nbtCompound);
        }
        return TrinketEnums.DropRule.KEEP;
    }
}
