package com.brainsmash.broken_world.blocks.entity.magical;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CrucibleBlockEntity extends BlockEntity {

    private static final FluidAmount SINGLE_TANK_CAPACITY = FluidAmount.BUCKET;

    public final SimpleFixedFluidInv fluidInv = new SimpleFixedFluidInv(1, SINGLE_TANK_CAPACITY);

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CRUCIBLE_ENTITY_TYPE, pos, state);
    }


    public ItemStack getFirstItem() {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                return inventory.get(i);
            }
        }
        return ItemStack.EMPTY;
    }

    public void removeFirstItem() {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                inventory.set(i, ItemStack.EMPTY);
                return;
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        fluidInv.toTag(nbt);
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluidInv.fromTag(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = new NbtCompound();
        writeNbt(compound);
        return compound;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public int getFluidColor() {
        if (fluidInv.getInvFluid(0).isEmpty()) return 0x7442FF;
        else return fluidInv.getInvFluid(0).getRenderColor();
    }

    public ItemStack insertItem(ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getItem().equals(stack.getItem())) {
                int insertCount = Math.min(inventory.get(i).getMaxCount() - inventory.get(i).getCount(),
                        stack.getCount());
                inventory.get(i).increment(insertCount);
                stack.decrement(insertCount);
            }
            if (stack.getCount() == 0) return ItemStack.EMPTY;
        }
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, stack);
                return ItemStack.EMPTY;
            }
        }
        if (stack.getCount() == 0) return ItemStack.EMPTY;
        return stack;
    }
}
