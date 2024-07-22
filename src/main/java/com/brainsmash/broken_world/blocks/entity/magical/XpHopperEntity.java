package com.brainsmash.broken_world.blocks.entity.magical;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.brainsmash.broken_world.registry.BlockRegister.XP_HOPPER_ENTITY_TYPE;

public class XpHopperEntity extends XpContainerEntity implements BlockEntityTicker<XpHopperEntity> {
    public XpHopperEntity(BlockPos pos, BlockState state) {
        super(XP_HOPPER_ENTITY_TYPE, pos, state);
    }


    @Override
    public void tick(World world, BlockPos pos, BlockState state, XpHopperEntity blockEntity) {
        
    }
}
