package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.entity.hostile.GlitchedSkeletonEntity;
import com.brainsmash.broken_world.entity.hostile.GlitchedZombieEntity;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class GlitchBlock extends Block {

    private final int range = 2;

    public GlitchBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = random.nextBetween(-range, range);
        int j = random.nextBetween(-range, range);
        int k = random.nextBetween(-range, range);
        BlockPos pos1 = pos.add(i, j, k);
        if (!world.getBlockState(pos1).isAir() && !world.getBlockState(pos1).isIn(
                TagKey.of(RegistryKeys.BLOCK, new Identifier("broken_world:antiglitch")))) {
            world.setBlockState(pos1, getDefaultState());
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient) {
            if (entity instanceof MobEntity mob) {
                if (entity instanceof ZombieEntity || entity instanceof ZombieVillagerEntity || entity instanceof DrownedEntity) {
                    GlitchedZombieEntity glitchedZombie = mob.convertTo(EntityRegister.GLITCHED_ZOMBIE_ENTITY_TYPE,
                            true);
                    assert glitchedZombie != null;
                    glitchedZombie.setCanPickUpLoot(true);
                    glitchedZombie.setCanBreakDoors(true);
                }
                if (entity instanceof SkeletonEntity || entity instanceof StrayEntity) {
                    GlitchedSkeletonEntity glitchedSkeleton = mob.convertTo(
                            EntityRegister.GLITCHED_SKELETON_ENTITY_TYPE, true);
                    assert glitchedSkeleton != null;
                    glitchedSkeleton.setCanPickUpLoot(true);
                }
            }
        }
    }
}
