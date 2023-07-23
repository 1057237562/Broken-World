package com.brainsmash.broken_world.items.electrical;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends Item {

    public int energy = 0;
    public boolean rechargeable;

    public BatteryItem(Settings settings, boolean rechargeable) {
        super(settings);
        this.rechargeable = rechargeable;
    }

    public int charge(ItemStack itemStack, int amount) {
        if (!rechargeable) return 0;
        int value = Math.min(itemStack.getDamage(), amount);
        itemStack.setDamage(itemStack.getDamage() - value);
        return value;
    }

    public int discharge(ItemStack itemStack, int amount) {
        int value = Math.min(itemStack.getMaxDamage() - itemStack.getDamage(), amount);
        itemStack.setDamage(itemStack.getDamage() + value);
        return value;
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
