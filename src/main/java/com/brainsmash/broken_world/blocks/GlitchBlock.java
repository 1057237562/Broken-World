package com.brainsmash.broken_world.blocks;

import com.brainsmash.broken_world.entity.hostile.GlitchedZombieEntity;
import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GlitchBlock extends Block {

    private final int range = 3;

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
                TagKey.of(Registry.BLOCK_KEY, new Identifier("broken_world:antiglitch")))) {
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
            }
        }
    }
}
