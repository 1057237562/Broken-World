package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class SolarPanelEntity extends PowerBlockEntity {
    public SolarPanelEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SOLAR_PANEL_ENTITY_TYPE, pos, state);
        setMaxCapacity(500);
        setGenerate(0);
        running = true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (world.isDay()) setGenerate((world.getLightLevel(LightType.SKY, pos) + 1) / 4);
            else setGenerate(0);
        }
        super.tick(world, pos, state, blockEntity);
    }
}
