package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.util.SerializationHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DummyBlockEntity extends BlockEntity {

    protected BlockState imitateBlock = Blocks.AIR.getDefaultState();
    protected BlockEntity imitateBlockEntity;
    protected NbtCompound imitateNbt;
    protected BlockPos link;
    public boolean visible = true;

    public DummyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public DummyBlockEntity(BlockPos pos, BlockState state) {
        super(MultiblockUtil.DUMMY_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("blockState", SerializationHelper.saveBlockState(imitateBlock));
        nbt.put("blockNbt", imitateNbt);
        nbt.putLong("link", link.asLong());
        nbt.putBoolean("visible", visible);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        BlockState blockState = SerializationHelper.loadBlockState(nbt.getCompound("blockState"));
        setImitateBlock(blockState, nbt.getCompound("blockNbt"));
        link = BlockPos.fromLong(nbt.getLong("link"));
        visible = nbt.getBoolean("visible");
    }

    public void setImitateBlock(BlockState blockState, NbtCompound nbtCompound) {
        imitateBlock = blockState;
        imitateNbt = nbtCompound;
        if (blockState.getBlock() instanceof BlockWithEntity bwe) {
            imitateBlockEntity = bwe.createBlockEntity(pos, blockState);
            imitateBlockEntity.setWorld(world);
            imitateBlockEntity.readNbt(nbtCompound);
        }
    }

    public void setLink(BlockPos pos) {
        link = pos;
    }

    public BlockPos getLink() {
        return link;
    }

    public BlockState getImitateBlockState() {
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

    public void disassemble() {
        if (world.getBlockEntity(link) instanceof MultiblockEntity mbe) {
            MultiblockUtil.revertToBlock(world, mbe.getAnchor(), mbe.getMultiblockSize());
        }
    }
}
