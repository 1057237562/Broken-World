package com.brainsmash.broken_world.items.magical.rune;

import com.brainsmash.broken_world.items.magical.Rune;
import com.brainsmash.broken_world.items.magical.enums.RuneEnum;
import com.brainsmash.broken_world.items.magical.util.ImplementedRune;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlacerRune extends Rune implements ImplementedRune {

    private Block block;

    public PlacerRune(Settings settings, RuneEnum type, Block placement) {
        super(settings, type);
        block = placement;
    }

    @Override
    public void execute(World world, PlayerEntity player, BlockPos at, ImplementedRune last) {
        world.setBlockState(at, block.getDefaultState());
    }
}
