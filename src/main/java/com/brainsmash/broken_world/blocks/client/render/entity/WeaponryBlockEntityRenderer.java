package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.WeaponryBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class WeaponryBlockEntityRenderer implements BlockEntityRenderer<WeaponryBlockEntity> {
    private static final String BODY = "body";
    private static final String ARM_STAND = "arm_stand";
    private static final String ARM = "arm";
    private static final String TOOL_LEFT = "tool_left";
    private static final String TOOL_RIGHT = "tool_right";
    private static final String PISTOL_CHAMBER = "pistol_chamber";
    private static final String PISTOL_GRIP = "pistol_grip";
    private static final String HANDLE_BASE_LEFT = "handle_base_left";
    private static final String HANDLE_LEFT = "handle_left";
    private static final String HANDLE_BASE_RIGHT = "handle_base_right";
    private static final String HANDLE_RIGHT = "handle_right";

    private static final Identifier TEXTURE_1 = new Identifier(Main.MODID, "textures/entity/weaponry/1.png");
    private static final Identifier TEXTURE_2 = new Identifier(Main.MODID, "textures/entity/weaponry/2.png");
    public static final EntityModelLayer WEAPONRY = new EntityModelLayer(new Identifier(Main.MODID, "weaponry"), "main");
    private final ModelPart body;
    private final ModelPart armStand;
    private final ModelPart arm;
    private final ModelPart toolLeft;
    private final ModelPart toolRight;
    private final ModelPart pistolChamber;
    private final ModelPart pistolGrip;
    private final ModelPart handleBaseLeft;
    private final ModelPart handleLeft;
    private final ModelPart handleBaseRight;
    private final ModelPart handleRight;
    private static float ticks = 0F;

    public WeaponryBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        ModelPart modelPart = ctx.getLayerModelPart(WEAPONRY);
        body = modelPart.getChild(BODY);
        armStand = modelPart.getChild(ARM_STAND);
        arm = modelPart.getChild(ARM);
        toolLeft = modelPart.getChild(TOOL_LEFT);
        toolRight = modelPart.getChild(TOOL_RIGHT);
        pistolChamber = modelPart.getChild(PISTOL_CHAMBER);
        pistolGrip = modelPart.getChild(PISTOL_GRIP);
        handleBaseLeft = modelPart.getChild(HANDLE_BASE_LEFT);
        handleLeft = modelPart.getChild(HANDLE_LEFT);
        handleBaseRight = modelPart.getChild(HANDLE_BASE_RIGHT);
        handleRight = modelPart.getChild(HANDLE_RIGHT);
    }
    public static TexturedModelData getTexturedModelData(){
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                BODY,
                ModelPartBuilder
                        .create()
                        .uv(0, 10)
                        .cuboid(0F, 0F, 0F, 16F, 6F, 16F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                ARM_STAND,
                ModelPartBuilder
                        .create()
                        .uv(0, 0)
                        .cuboid(1F, 6F, 1F, 2F, 13F, 2F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                ARM,
                ModelPartBuilder
                        .create()
                        .uv(0, 0)
                        .cuboid(1F, 13F, 1F, 2F, 2F, 10F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                TOOL_RIGHT,
                ModelPartBuilder
                        .create()
                        .uv(8, 0)
                        .cuboid(3F, 11F, 8F, 1F, 4F, 2F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                TOOL_LEFT,
                ModelPartBuilder
                        .create()
                        .uv(8, 0)
                        .cuboid(0F, 11F, 8F, 1F, 4F, 2F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                PISTOL_GRIP,
                ModelPartBuilder
                        .create()
                        .uv(44, 0)
                        .cuboid(10F, 6F, 8F, 2F, 1F, 3F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                PISTOL_CHAMBER,
                ModelPartBuilder
                        .create()
                        .uv(14, 0)
                        .cuboid(5F, 6F, 6F, 7F, 1F, 2F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                HANDLE_BASE_RIGHT,
                ModelPartBuilder
                        .create()
                        .uv(32, 0)
                        .cuboid(13F, 6F, 13F, 2F, 1F, 2F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                HANDLE_BASE_LEFT,
                ModelPartBuilder
                        .create()
                        .uv(32, 0)
                        .cuboid(1F, 6F, 13F, 2F, 1F, 2F),
                ModelTransform.NONE
        );
        modelPartData.addChild(
                HANDLE_RIGHT,
                ModelPartBuilder
                        .create()
                        .uv(40, 0)
                        .cuboid(13.5F, 6F, 13.5F, 1F, 3F, 1F),
                ModelTransform.of(14, 7, 14, 22.5F, 0, 0)
        );
        modelPartData.addChild(
                HANDLE_LEFT,
                ModelPartBuilder
                        .create()
                        .uv(40, 0)
                        .cuboid(1.5F, 6F, 13.5F, 1F, 3F, 1F),
                ModelTransform.of(2, 7, 14, -22.5F, 0, 0)
        );
        return TexturedModelData.of(modelData, 128, 64);
    }

    private float f(float tick) {
        final float travelRight = 40F;
        final float travelLeft = 20F;
        final float pause = 50F;

        tick %= travelRight + travelLeft + pause;
        if (tick <= travelRight)
            return 12F * tick / travelRight;
        if (tick <= travelRight + travelLeft)
            return 12F - 12F * (tick-travelLeft) / travelRight;
        else return 0;
    }

    @Override
    public void render(WeaponryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer1 = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_1));
        VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_2));
        ticks += tickDelta;

        body.render(matrices, vertexConsumer1, light, overlay);

        Direction facing = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
        Direction travelDirection = facing.rotateYCounterclockwise();
        Vec3f travel = travelDirection.getUnitVector();
        travel.scale(f(ticks));

        armStand.translate(travel);
        armStand.render(matrices, vertexConsumer1, light, overlay);
        arm.translate(travel);
        arm.render(matrices, vertexConsumer2, light, overlay);
        toolLeft.translate(travel);
        toolLeft.render(matrices, vertexConsumer1, light, overlay);
        toolRight.translate(travel);
        toolRight.render(matrices, vertexConsumer1, light, overlay);


        pistolChamber.render(matrices, vertexConsumer1, light, overlay);
        pistolGrip.render(matrices, vertexConsumer1, light, overlay);
        handleBaseLeft.render(matrices, vertexConsumer1, light, overlay);
        handleLeft.render(matrices, vertexConsumer1, light, overlay);
        handleBaseRight.render(matrices, vertexConsumer1, light, overlay);
        handleRight.render(matrices, vertexConsumer1, light, overlay);

        matrices.pop();
    }
}
