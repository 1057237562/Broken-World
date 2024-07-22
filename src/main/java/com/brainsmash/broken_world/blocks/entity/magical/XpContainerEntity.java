package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.registry.FluidRegister;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class XpContainerEntity extends BlockEntity {

    public final SingleFluidStorage<FluidVariant> xpStorage = new SingleFluidStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.of(FluidRegister.still_fluid[6]);
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return 4 * FluidConstants.BUCKET;
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return variant.getFluid().matchesType(FluidRegister.still_fluid[6].getStill());
        }

        @Override
        protected boolean canExtract(FluidVariant variant) {
            return variant.getFluid().matchesType(FluidRegister.still_fluid[6].getStill());
        }
    };

    public XpContainerEntity(BlockEntityType<? extends XpContainerEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        xpStorage.variant = FluidVariant.of(FluidRegister.still_fluid[6]);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("xpStorage", xpStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        xpStorage.amount = nbt.getLong("xpStorage");
    }
}
