package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class DummyBlockEntity extends BlockEntity {

    protected Block imitateBlock = Blocks.AIR;
    protected BlockEntity imitateBlockEntity;
    protected NbtCompound imitateNbt;


    public DummyBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.DUMMY_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("blockId", Registry.BLOCK.getId(imitateBlock).toString());
        nbt.put("blockNbt", imitateNbt);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setImitateBlock(Registry.BLOCK.get(Identifier.tryParse(nbt.getString("blockId"))), nbt.getCompound("blockNbt"));
    }

    public void setImitateBlock(Block block, NbtCompound nbtCompound) {
        imitateBlock = block;
        imitateNbt = nbtCompound;
        if (block instanceof BlockWithEntity bwe) {
            imitateBlockEntity = bwe.createBlockEntity(pos, block.getDefaultState());
            imitateBlockEntity.setWorld(world);
            imitateBlockEntity.readNbt(nbtCompound);
        }
    }

    public Block getImitateBlock() {
        return imitateBlock;
    }

    public BlockEntity getImitateBlockEntity() {
        return imitateBlockEntity;
    }

    public NbtCompound getImitateNbt() {
        return imitateNbt;
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
