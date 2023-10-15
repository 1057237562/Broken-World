package com.brainsmash.broken_world.items.magical.rune;

import com.brainsmash.broken_world.items.magical.Rune;
import com.brainsmash.broken_world.items.magical.enums.RuneEnum;
import com.brainsmash.broken_world.items.magical.util.ImplementedRune;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SummonRune extends Rune implements ImplementedRune {
    private EntityType<?> entityType;

    public SummonRune(Settings settings, RuneEnum type, EntityType<?> entity) {
        super(settings, type);
        entityType = entity;
    }

    @Override
    public void execute(World world, PlayerEntity player, BlockPos at, ImplementedRune last) {
        world.spawnEntity(entityType.create(world));
    }
}
