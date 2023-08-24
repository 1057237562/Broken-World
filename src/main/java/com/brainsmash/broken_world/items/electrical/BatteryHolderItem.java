package com.brainsmash.broken_world.items.electrical;

import com.brainsmash.broken_world.util.ItemHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryHolderItem extends Item {
    public static final String ENERGY_KEY = BatteryItem.ENERGY_KEY;
    public static final String MAX_ENERGY_KEY = BatteryItem.MAX_ENERGY_KEY;
    public static final String ID_KEY = BatteryItem.ID_KEY;
    public static final String BATTERY_KEY = "battery";

    public BatteryHolderItem(Settings settings) {
        super(settings.maxCount(1).maxDamage(-1));
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public int getEnergy(ItemStack stack) {
        NbtCompound batteryNbt = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        return batteryNbt.isEmpty() ? 0 : batteryNbt.getInt(ENERGY_KEY);
    }

    public Pair<Integer, Integer> getEnergyPair(ItemStack stack) {
        NbtCompound batteryNbt = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        return batteryNbt.isEmpty() ? new Pair<>(0, 0) : new Pair<>(batteryNbt.getInt(ENERGY_KEY), batteryNbt.getInt(MAX_ENERGY_KEY));
    }

    public boolean hasBattery(ItemStack stack) {
        return !stack.getOrCreateNbt().getCompound(BATTERY_KEY).isEmpty();
    }

    public boolean insertBattery(ItemStack stack, ItemStack otherStack) {
        if (!(stack.getItem() instanceof BatteryHolderItem) || hasBattery(stack) || !(otherStack.getItem() instanceof BatteryItem batteryItem))
            return false;

        stack.getOrCreateNbt().put(BATTERY_KEY, batteryItem.getBatteryNbt(otherStack));
        otherStack.setCount(0);
        return true;
    }

    public ItemStack extractBattery(ItemStack stack) {
        if (!(stack.getItem() instanceof BatteryHolderItem batteryHolderIte) || !hasBattery(stack))
            return ItemStack.EMPTY;

        NbtCompound nbt = stack.getOrCreateNbt();
        NbtCompound batteryNbt = nbt.getCompound(BATTERY_KEY);
        if (Registry.ITEM.get(new Identifier(batteryNbt.getString(ID_KEY))) instanceof BatteryItem batteryItem) {
            ItemStack result = batteryItem.getStackFromNbt(batteryNbt);
            if (!result.isEmpty())
                nbt.remove(BATTERY_KEY);
            return result;
        }
        return ItemStack.EMPTY;
    }

    public int discharge(ItemStack stack, int amount) {
        NbtCompound batteryNBT = stack.getOrCreateNbt().getCompound(BATTERY_KEY);
        if (batteryNBT.isEmpty() || amount <= 0)
            return 0;
        int e1 = batteryNBT.getInt(ENERGY_KEY);
        int e2 = MathHelper.clamp(e1-amount, 0, batteryNBT.getInt(MAX_ENERGY_KEY));
        batteryNBT.putInt(ENERGY_KEY, e2);
        return e2-e1;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference))
            return true;
        if (clickType != ClickType.RIGHT)
            return false;

        boolean hasBattery = hasBattery(stack);
        if (hasBattery && otherStack.isEmpty()) {
            player.giveItemStack(extractBattery(stack));
        } else if (!hasBattery && otherStack.getItem() instanceof BatteryItem battery) {
            return insertBattery(stack, otherStack);
        }
        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        Pair<Integer, Integer> energyPair = getEnergyPair(stack);
        return ItemHelper.calculateItemBarStep(energyPair.getLeft(), energyPair.getRight());
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        Pair<Integer, Integer> energyPair = getEnergyPair(stack);
        return ItemHelper.calculateItemBarColor(energyPair.getLeft(), energyPair.getRight());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (hasBattery(stack)) {
            Pair<Integer, Integer> pair = getEnergyPair(stack);
            tooltip.add(
                    Text.literal(pair.getLeft() + "/" + pair.getRight() + " IU").formatted(
                            Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("no_battery").formatted(Formatting.GRAY));
        }
    }
}
