package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class StoneBaseBlockEntity extends BlockEntity {

    public ItemStack itemStack = ItemStack.EMPTY;
    public float tick = 0;

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("item", itemStack.writeNbt(new NbtCompound()));
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        itemStack = ItemStack.fromNbt(nbt.getCompound("item"));
    }

    public StoneBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.STONE_BASE_ENTITY_TYPE, pos, state);
    }

    public void startCrafting() {
        if (!world.isClient) {

        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = new NbtCompound();
        writeNbt(compound);
        return compound;
    }
}
