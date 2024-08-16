package com.brainsmash.broken_world.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Redirect(method = "method_17761", at = @At(value = "INVOKE", target = "net/minecraft/block/entity/AbstractFurnaceBlockEntity.dropExperience (Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;IF)V"))
    private static void removeAllFurnaceExpGain(ServerWorld world, Vec3d pos, int multiplier, float experience) {
    }
}
