package com.brainsmash.broken_world.blocks.entity.magical;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CrucibleBlockEntity extends BlockEntity {

    private static final FluidAmount SINGLE_TANK_CAPACITY = FluidAmount.BUCKET;

    public final SimpleFixedFluidInv fluidInv = new SimpleFixedFluidInv(1, SINGLE_TANK_CAPACITY);

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CRUCIBLE_ENTITY_TYPE, pos, state);
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        fluidInv.toTag(nbt);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluidInv.fromTag(nbt);
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
}
