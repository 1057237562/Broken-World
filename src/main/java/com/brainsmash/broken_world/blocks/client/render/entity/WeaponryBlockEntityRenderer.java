package com.brainsmash.broken_world.blocks.client.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.CreativeGeneratorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

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

    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/weaponry.png");
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
    private static final long CYCLE = 48;

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
                        .uv(64, 0)
                        .cuboid(0F, 0F, 0F, 16F, 6F, 16F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                ARM_STAND,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(4F, 6F, 1F, 6F, 13F, 3F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                ARM,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(4F, 13F, 1F, 6F, 15F, 11F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                TOOL_RIGHT,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(6F, 11F, 8F, 7F, 15F, 10F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                TOOL_LEFT,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(3F, 11F, 8F, 4F, 15F, 10F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                PISTOL_GRIP,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(10F, 6F, 8F, 12F, 7F, 11F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                PISTOL_CHAMBER,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(5F, 6F, 6F, 12F, 7F, 8F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                HANDLE_BASE_RIGHT,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(13F, 6F, 13F, 15F, 7F, 15F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                HANDLE_BASE_LEFT,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(1F, 6F, 13F, 3F, 7F, 15F),
                ModelTransform.pivot(8.0f, 8.0f, 8.0f)
        );
        modelPartData.addChild(
                HANDLE_RIGHT,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(13.5F, 6F, 13.5F, 14.5F, 9F, 14.5F),
                ModelTransform.of(14, 7, 14, 22.5F, 0, 0)
        );
        modelPartData.addChild(
                HANDLE_LEFT,
                ModelPartBuilder
                        .create()
                        .uv(64, 0)
                        .cuboid(1.5F, 6F, 13.5F, 2.5F, 9F, 14.5F),
                ModelTransform.of(2, 7, 14, -22.5F, 0, 0)
        );
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void render(CreativeGeneratorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        shell.render(matrices, vertexConsumer, 15728880, overlay);
        frame.render(matrices, vertexConsumer, light, overlay);
        long time = entity.getWorld().getTime();
        float scale = 0.1f*(float)Math.sin((double)time/CYCLE*2*Math.PI);
        midOrb.xScale = midOrb.yScale = midOrb.zScale = 1.0f + scale;
        scale *= 0.5f;
        innerOrb.xScale = innerOrb.yScale = innerOrb.zScale = 1.0f + scale;
        midOrb.render(matrices, vertexConsumer, 15728880, overlay);
        innerOrb.render(matrices, vertexConsumer, 15728880, overlay);
        matrices.pop();
    }
}
