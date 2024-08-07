package com.brainsmash.broken_world.blocks.entity;

import com.brainsmash.broken_world.blocks.CutRubberLogBlock;
import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static com.brainsmash.broken_world.blocks.CutRubberLogBlock.RUBBER_LEVEL;

public class WoodenPipeBlockEntity extends BlockEntity implements BlockEntityTicker<WoodenPipeBlockEntity> {

    public final SingleFluidStorage<FluidVariant> fluidStorage = new SingleFluidStorage() {
        @Override
        protected TransferVariant<?> getBlankVariant() {
            return FluidVariant.of(FluidRegister.still_fluid[5]);
        }

        @Override
        protected long getCapacity(TransferVariant variant) {
            return FluidConstants.BUCKET;
        }
    };

    public WoodenPipeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WOODEN_PIPE_ENTITY_TYPE, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state, WoodenPipeBlockEntity blockEntity) {
        if (!world.isClient) {
            if (!fluidStorage.isEmpty()) {
                Storage<FluidVariant> insertable = FluidStorage.SIDED.find(world, pos.offset(Direction.DOWN),
                        Direction.UP);
                if (insertable != null) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        fluidStorage.amount -= insertable.insert(fluidStorage.variant, fluidStorage.amount,
                                transaction);
                        markDirty();
                        transaction.commit();
                    }
                }
            }
            Direction direction = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING);
            BlockState blockState = world.getBlockState(pos.offset(direction));
            if (blockState.getBlock() instanceof CutRubberLogBlock cut) {
                if (blockState.get(RUBBER_LEVEL) >= 2) {
                    if (fluidStorage.isEmpty()) {
                        fluidStorage.amount = FluidConstants.BOTTLE;
                        world.setBlockState(pos.offset(direction), blockState.with(RUBBER_LEVEL, 0));
                    } else if (fluidStorage.amount <= FluidConstants.BOTTLE * 2) {
                        fluidStorage.amount += FluidConstants.BOTTLE;
                        world.setBlockState(pos.offset(direction), blockState.with(RUBBER_LEVEL, 0));
                    }
                }
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("amount", fluidStorage.amount);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluidStorage.amount = nbt.getLong("amount");
    }
}
