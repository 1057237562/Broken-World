package com.brainsmash.broken_world.items.electrical;

import com.brainsmash.broken_world.util.ItemHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends Item {

    protected int energy = 0;
    protected final int maxEnergy;
    public boolean rechargeable;

    public BatteryItem(Settings settings, int maxEnergy, boolean rechargeable) {
        super(settings);
        this.maxEnergy = maxEnergy;
        this.rechargeable = rechargeable;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int charge(int amount) {
        if (!rechargeable) return 0;
        int value = Math.min(maxEnergy - energy, amount);
        energy += value;
        return value;
    }

    public int discharge(int amount) {
        int value = Math.min(energy, amount);
        energy -= value;
        return value;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return ItemHelper.calculateItemBarStep(energy, maxEnergy);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ItemHelper.calculateItemBarColor(energy, maxEnergy);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(
                Text.literal((stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage() + " IU").formatted(
                        Formatting.GRAY));
    }
}
