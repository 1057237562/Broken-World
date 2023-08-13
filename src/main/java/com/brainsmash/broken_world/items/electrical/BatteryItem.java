package com.brainsmash.broken_world.items.electrical;

import com.brainsmash.broken_world.util.ItemHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends Item implements EnergyItem {
    protected final int maxEnergy;
    public boolean rechargeable;

    public BatteryItem(Settings settings, int maxEnergy, boolean rechargeable) {
        super(settings);
        this.maxEnergy = maxEnergy;
        this.rechargeable = rechargeable;
    }



    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return ItemHelper.calculateItemBarStep(getEnergy(stack), maxEnergy);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ItemHelper.calculateItemBarColor(getEnergy(stack), maxEnergy);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        NbtCompound batteryNbt = new NbtCompound();
        batteryNbt.putInt(ENERGY_KEY, 0);
        batteryNbt.putInt(MAX_ENERGY_KEY, maxEnergy);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(
                Text.literal((stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage() + " IU").formatted(
                        Formatting.GRAY));
    }
}
