package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CreativeBatteryBlockEntity extends BatteryBlockEntity {
    private Entity chargedCreeper;
    public float tick = 0;

    public CreativeBatteryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CREATIVE_BATTERY_ENTITY_TYPE, pos, state);
        setMaxCapacity(1000000);
    }

    public Entity getCreeper() {
        if (chargedCreeper == null) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", "creeper");
            nbt.putBoolean("powered", true);
            chargedCreeper = EntityType.loadEntityWithPassengers(nbt, getWorld(), Function.identity());
        }
        return chargedCreeper;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
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
