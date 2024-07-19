package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.blocks.magical.CrucibleBlock;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.model.quad.blender.LinearColorBlender;
import me.jellysquid.mods.sodium.client.util.color.ColorARGB;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LinearColorBlender.class)
public abstract class SodiumBlendMixin {

    @Shadow(remap = false)
    protected abstract <T> int getBlockColor(BlockRenderView world, T state, ColorSampler<T> sampler, int x, int y, int z, int colorIndex);

    @Inject(method = "getVertexColor", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private <T> void onGetVertexColor(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state, int vertexIdx, CallbackInfoReturnable<Integer> cir) {
        if (state instanceof BlockState && ((BlockState) state).getBlock() instanceof CrucibleBlock) {
            int color = getBlockColor(world, state, sampler, origin.getX(), origin.getY(), origin.getZ(),
                    quad.getColorIndex());
            cir.setReturnValue(ColorARGB.toABGR(color));
        }
    }
}
