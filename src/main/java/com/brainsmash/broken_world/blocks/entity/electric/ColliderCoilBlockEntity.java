package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColliderCoilBlockEntity extends ConsumerBlockEntity {

    protected boolean priming = false;
    protected ColliderControllerBlockEntity controllerEntity = null;
    public static final int MAX_PROGRESSION = 80;

    public ColliderCoilBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.COLLIDER_COIL_ENTITY_TYPE, pos, state);
        setMaxCapacity(100);
        powerConsumption = 10;
        maxProgression = MAX_PROGRESSION;
    }

    public float getChargePercentage() {
        return (float) progression / maxProgression;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient()) {
            running = priming && checkEnergy() && !isCharged();
            if (isRunning() && progression < maxProgression) {
                progression++;
                markDirty();
            }
        }
        super.tick(world, pos, state, blockEntity);
    }

    public boolean isCharged() {
        return progression == maxProgression;
    }

    public void start() {
        if (!priming) markDirty();
        priming = true;
    }

    public void stop() {
        if (priming) markDirty();
        priming = false;
    }

    public void discharge() {
        progression = 0;
    }

    public void bindController(ColliderControllerBlockEntity controller) {
        if (controllerEntity == null) controllerEntity = controller;
    }

    public boolean hasBoundController() {
        return controllerEntity != null;
    }

    public void unbindController() {
        controllerEntity = null;
    }

    public void onMultiblockBreak() {
        if (controllerEntity != null) {
            controllerEntity.onMultiblockBreak();
            controllerEntity = null;
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        priming = nbt.getBoolean("priming");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("priming", priming);
        super.writeNbt(nbt);
    }
}
