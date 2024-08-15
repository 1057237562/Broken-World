package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.guns.Rifle;
import com.brainsmash.broken_world.items.weapons.guns.SniperRifle;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "HEAD"))
    private void gunRecoil(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
        if (player instanceof PlayerDataExtension dataExtension) {
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(dataExtension.getPitchSpeed() * 0.5f));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(dataExtension.getYawSpeed() * 0.5f));
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", ordinal = 0, shift = At.Shift.AFTER))
    private void gunHand(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!player.isUsingItem()) {
            Item handItem = item.getItem();
            matrices.push();
            if (handItem instanceof Rifle) {
                Arm arm = hand == Hand.OFF_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(10));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-20));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-50));
                this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            }
            if (handItem instanceof SniperRifle) {
                Arm arm = hand == Hand.OFF_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                matrices.translate(0.2f, 0.1f, 0.3f);
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(10));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-30));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-60));
                this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            }
            matrices.pop();
        }

    }
}
