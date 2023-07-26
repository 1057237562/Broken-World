package com.brainsmash.broken_world.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlockMixin {

    @Inject(method = "onBreak", at = @At("HEAD"))
    public void checkMultiblock(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {

    }
}
