package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColliderCoilBlockEntity extends ConsumerBlockEntity {

    public ColliderCoilBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.COLLIDER_COIL_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        powerConsumption = 100;
        maxProgression = 80;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (isRunning() && progression < maxProgression)
            progression++;
        super.tick(world, pos, state, blockEntity);
    }

    public boolean isCharged() {
        return getEnergy() == getMaxCapacity();
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void discharge() {
        progression = 0;
    }
}
