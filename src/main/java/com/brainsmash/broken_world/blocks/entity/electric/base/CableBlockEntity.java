package com.brainsmash.broken_world.blocks.entity.electric.base;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CableBlockEntity extends BlockEntity implements BlockEntityTicker<CableBlockEntity> {

    public Map<Direction, Integer> edges = new ConcurrentHashMap<>();
    public Direction parent = null;
    public int deltaFlow = 0;
    public int minFlow = 0;
    private int energy = 0;
    private int maxCapacity = 0;
    private int maxFlow = 16;
    public boolean tickMark = false;
    public boolean visMark = false;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CABLE_ENTITY_TYPE, pos, state);
    }

    public CableBlockEntity(BlockPos pos, BlockState state, int flow) {
        super(BlockRegister.CABLE_ENTITY_TYPE, pos, state);
        maxFlow = flow;
    }

    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int cap) {
        maxCapacity = cap;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
    }

    BlockEntity getAdjacentBlockEntity(Direction direction) {
        return world.getBlockEntity(getPos().offset(direction));
    }

    public void setEnergy(int i) {
        energy = i;
        markDirty();
    }

    public void increaseEnergy(int i) {
        energy += i;
        setEnergy(Math.min(energy, getMaxCapacity()));
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        energy = compound.getInt("energy");
        deltaFlow = compound.getInt("deltaFlow");
        maxCapacity = compound.getInt("maxCapacity");
        maxFlow = compound.getInt("maxFlow");
        edges.put(Direction.NORTH, compound.getInt("north"));
        edges.put(Direction.SOUTH, compound.getInt("south"));
        edges.put(Direction.WEST, compound.getInt("west"));
        edges.put(Direction.EAST, compound.getInt("east"));
        edges.put(Direction.UP, compound.getInt("up"));
        edges.put(Direction.DOWN, compound.getInt("down"));
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        compound.putInt("energy", energy);
        compound.putInt("deltaFlow", deltaFlow);
        compound.putInt("maxCapacity", maxCapacity);
        compound.putInt("maxFlow", maxFlow);
        compound.putInt("north", edges.getOrDefault(Direction.NORTH, 0));
        compound.putInt("south", edges.getOrDefault(Direction.SOUTH, 0));
        compound.putInt("west", edges.getOrDefault(Direction.WEST, 0));
        compound.putInt("east", edges.getOrDefault(Direction.EAST, 0));
        compound.putInt("up", edges.getOrDefault(Direction.UP, 0));
        compound.putInt("down", edges.getOrDefault(Direction.DOWN, 0));
        super.writeNbt(compound);
    }

    public void ComputeDeltaFlow() {
        deltaFlow = 0;
        for (Direction direction : Direction.values()) {
            if (getAdjacentBlockEntity(direction) instanceof CableBlockEntity neighbour) {
                deltaFlow += (edges.getOrDefault(direction, 0) - neighbour.edges.getOrDefault(direction.getOpposite(),
                        0)) / 2;
            }
        }
        markDirty();
    }

    public int currentFlow() {
        int sum = 0;
        for (Direction direction : Direction.values()) {
            sum += edges.getOrDefault(direction, 0);
        }
        return sum >> 1;
    }
}
