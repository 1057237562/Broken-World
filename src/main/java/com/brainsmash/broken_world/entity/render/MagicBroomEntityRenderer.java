package com.brainsmash.broken_world.entity.render;

import com.brainsmash.broken_world.entity.model.MagicBroomEntityModel;
import com.brainsmash.broken_world.entity.vehicle.MagicBroomEntity;
import com.brainsmash.broken_world.registry.EntityModelLayerRegister;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import static com.brainsmash.broken_world.Main.MODID;

public class MagicBroomEntityRenderer extends EntityRenderer<MagicBroomEntity> {

    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/magic_broom.png");
    private final MagicBroomEntityModel model;

    public MagicBroomEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new MagicBroomEntityModel<>(ctx.getPart(EntityModelLayerRegister.MODEL_MAGIC_BROOM));
    }

    @Override
    public Identifier getTexture(MagicBroomEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MagicBroomEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0, -0.1f, 0);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - entity.getBodyYaw()));
        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1,
                1, 1, 1);
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
