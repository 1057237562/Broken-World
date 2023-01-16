package com.brainsmash.broken_world.blocks.entity.electric.generator;

import com.brainsmash.broken_world.blocks.electric.generator.WindTurbineBlock;
import com.brainsmash.broken_world.blocks.entity.electric.base.EnergyManager;
import com.brainsmash.broken_world.blocks.entity.electric.base.PowerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WindTurbineEntity extends PowerBlockEntity {
    public WindTurbineEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.WIND_TURBINE_ENTITY_TYPE, pos, state);
        setMaxCapacity(2000);
        setGenerate(0);
    }

}
