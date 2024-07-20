package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

public class XpCropBlock extends CropBlock {
    public XpCropBlock() {
        super(AbstractBlock.Settings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(
                BlockSoundGroup.CROP));
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.BONE_BLOCK) || floor.isOf(BlockRegister.get(BlockRegistry.COMPRESSED_BONE_BLOCK));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean hasEnoughLight = world.getBaseLightLevel(pos, 0) >= 9;
        int age = this.getAge(state);
        boolean notMature = age < this.getMaxAge();
        int chance = world.getBlockState(pos.down()).isOf(
                BlockRegister.get(BlockRegistry.COMPRESSED_BONE_BLOCK)) ? 50 : 33;
        if (hasEnoughLight && notMature && random.nextInt(100) < chance) {
            world.setBlockState(pos, this.withAge(age + 1), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }
}
