package com.brainsmash.broken_world.blocks.entity;

import alexiil.mc.lib.attributes.CombinableAttribute;
import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.brainsmash.broken_world.blocks.CutRubberLogBlock;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.brainsmash.broken_world.blocks.CutRubberLogBlock.RUBBER_LEVEL;

public class WoodenPipeBlockEntity extends BlockEntity {

    private FluidVolume stored = FluidVolumeUtil.EMPTY;

    public WoodenPipeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WOODEN_PIPE_ENTITY_TYPE, pos, state);
    }

    @Nonnull
    public <T> T getNeighbourAttribute(CombinableAttribute<T> attr, Direction dir) {
        return attr.get(getWorld(), getPos().offset(dir), SearchOptions.inDirection(dir));
    }

    public void tick(World world, BlockPos pos, BlockState state, WoodenPipeBlockEntity blockEntity) {
        if (!world.isClient) {
            if (!stored.isEmpty()) {
                FluidInsertable insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, Direction.DOWN);
                stored = insertable.attemptInsertion(stored, Simulation.ACTION);
                markDirty();
            }
            Direction direction = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING);
            BlockState blockState = world.getBlockState(pos.offset(direction));
            if (blockState.getBlock() instanceof CutRubberLogBlock cut) {
                if (blockState.get(RUBBER_LEVEL) >= 2) {
                    if (stored.isEmpty()) {
                        stored = FluidKeys.get(FluidRegister.still_fluid[5]).withAmount(FluidAmount.BOTTLE);
                        world.setBlockState(pos.offset(direction), blockState.with(RUBBER_LEVEL, 0));
                    } else if (stored.canMerge(FluidKeys.get(FluidRegister.still_fluid[5]).withAmount(
                            FluidAmount.BOTTLE)) && stored.amount().isGreaterThanOrEqual(FluidAmount.of(2, 3))) {
                        stored.merge(FluidKeys.get(FluidRegister.still_fluid[5]).withAmount(FluidAmount.BOTTLE),
                                Simulation.ACTION);
                        world.setBlockState(pos.offset(direction), blockState.with(RUBBER_LEVEL, 0));
                    }
                }
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("fluid", stored.toTag());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        stored = FluidVolume.fromTag(nbt.getCompound("fluid"));
    }
}
