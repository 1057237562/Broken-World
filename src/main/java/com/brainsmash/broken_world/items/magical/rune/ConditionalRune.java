package com.brainsmash.broken_world.items.magical.rune;

import com.brainsmash.broken_world.items.magical.Rune;
import com.brainsmash.broken_world.items.magical.enums.RuneEnum;
import com.brainsmash.broken_world.items.magical.util.ImplementedRune;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConditionalRune extends Rune implements ImplementedRune {
    public ConditionalRune(Settings settings, RuneEnum type) {
        super(settings, type);
    }

    @Override
    public void execute(World world, PlayerEntity player, BlockPos at, ImplementedRune last) {

    }
}
