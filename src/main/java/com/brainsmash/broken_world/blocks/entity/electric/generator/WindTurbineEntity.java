package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WindTurbineEntity extends PowerBlockEntity {

    protected int nearbyTurbineCount = 0;
    public final int maxOutput = 20;
    public WindTurbineEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WIND_TURBINE_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
    }

    private double sigmoid(int x, int s, int e){
        double f = 10.0*x/(e-s);
        return 1/(1+Math.exp(-f));
    }

    public void randomTick(Random random){
        setGenerate((int)((1+0.25*random.nextGaussian())*maxOutput*sigmoid(pos.getY() - world.getSeaLevel(),0,world.getTopY()-world.getSeaLevel())));
        if(nearbyTurbineCount == 0)
            running = true;
        markDirty();
    }

    public void moreCrowded(){
        running = false;
        nearbyTurbineCount++;
        markDirty();
    }

    public void lessCrowded(){
        nearbyTurbineCount--;
        markDirty();
        if(nearbyTurbineCount == 0) {
            randomTick(world.getRandom());
            running = true;
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("nearby",nearbyTurbineCount);
        nbt.putInt("powerGen",getGenerate());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        nearbyTurbineCount = nbt.getInt("nearby");
        setGenerate(nbt.getInt("powerGen"));
        if(nearbyTurbineCount == 0)
            running = true;
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
