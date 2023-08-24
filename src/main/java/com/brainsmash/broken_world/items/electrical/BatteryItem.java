package com.brainsmash.broken_world.items.electrical;

import com.brainsmash.broken_world.util.ItemHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends Item {
    public static final String MAX_ENERGY_KEY = "maxEnergy";
    public static final String RECHARGEABLE_KEY = "rechargeable";
    public static final String ID_KEY = "id";
    public static final String ENERGY_KEY = "energy";
    public final int maxEnergy;
    public final boolean rechargeable;

    public BatteryItem(Settings settings, int maxEnergy, boolean rechargeable) {
        super(settings.maxCount(1));
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
        stack.getOrCreateNbt().putInt(ENERGY_KEY, 0);
    }

    public int getEnergy(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(ENERGY_KEY);
    }

    public int discharge(ItemStack stack, int value) {
        return value < 0 ? 0 : addEnergy(stack, -value);
    }

    public int charge(ItemStack stack, int value) {
        return value < 0 ? 0 : addEnergy(stack, value);
    }

    public int addEnergy(ItemStack stack, int value) {
        if (value > 0 && !rechargeable)
            return 0;
        NbtCompound nbt = stack.getOrCreateNbt();
        int e1 = nbt.getInt(ENERGY_KEY);
        int e2 = MathHelper.clamp(e1+value, 0, maxEnergy);
        nbt.putInt(ENERGY_KEY, e2);
        return e2-e1;
    }

    /**
     * Gets battery NBT for storing the battery in {@link BatteryHolderItem}.
     * @param stack The stack that has the battery
     * @return {@link NbtCompound} for storing in BatteryHolderItem.
     */
    public NbtCompound getBatteryNbt(ItemStack stack) {
        Identifier id = Registry.ITEM.getId(this);
        if (id == null) {
            throw new RuntimeException("Cannot obtain battery NBT from not registered battery item. ");
        }
        int e = stack.getOrCreateNbt().getInt(ENERGY_KEY);
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(ENERGY_KEY, e);
        nbt.putInt(MAX_ENERGY_KEY, maxEnergy);
        nbt.putBoolean(RECHARGEABLE_KEY, rechargeable);
        nbt.putString(ID_KEY, id.toString());
        return nbt;
    }

    /**
     * Gets a battery item stack from battery NBT, which is used for storing in {@link BatteryHolderItem}.
     * @param nbt NBT that is used for storing in {@link BatteryHolderItem}
     * @return {@link ItemStack} that holds a battery.
     */
    protected ItemStack getStackFromNbt(NbtCompound nbt) {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt(ENERGY_KEY, nbt.getInt(ENERGY_KEY));
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        int energy = getEnergy(stack);
        tooltip.add(
                Text.literal(energy + "/" + maxEnergy + " IU").formatted(
                        Formatting.GRAY));
    }
}
