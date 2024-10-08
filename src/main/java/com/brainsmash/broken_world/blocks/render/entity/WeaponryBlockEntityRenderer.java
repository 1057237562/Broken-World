package com.brainsmash.broken_world.blocks.render.entity;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.WeaponryBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;

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

    private static final Identifier TEXTURE = new Identifier(Main.MODID, "textures/entity/weaponry.png");
    public static final EntityModelLayer WEAPONRY = new EntityModelLayer(new Identifier(Main.MODID, "weaponry"),
            "main");
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

    public WeaponryBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
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

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(BODY, ModelPartBuilder.create().uv(0, 10).cuboid(0F, 0F, 0F, 16F, 6F, 16F),
                ModelTransform.NONE);
        modelPartData.addChild(ARM_STAND, ModelPartBuilder.create().uv(0, 0).cuboid(1F, 6F, 1F, 2F, 7F, 2F),
                ModelTransform.NONE);
        modelPartData.addChild(ARM, ModelPartBuilder.create().uv(0, 32).cuboid(1F, 13F, 1F, 2F, 2F, 10F),
                ModelTransform.NONE);
        modelPartData.addChild(TOOL_RIGHT, ModelPartBuilder.create().uv(8, 0).cuboid(3F, 11F, 8F, 1F, 4F, 2F),
                ModelTransform.NONE);
        modelPartData.addChild(TOOL_LEFT, ModelPartBuilder.create().uv(8, 0).cuboid(0F, 11F, 8F, 1F, 4F, 2F),
                ModelTransform.NONE);
        modelPartData.addChild(PISTOL_GRIP, ModelPartBuilder.create().uv(44, 0).cuboid(10F, 6F, 8F, 2F, 1F, 3F),
                ModelTransform.NONE);
        modelPartData.addChild(PISTOL_CHAMBER, ModelPartBuilder.create().uv(14, 0).cuboid(5F, 6F, 6F, 7F, 1F, 2F),
                ModelTransform.NONE);
        modelPartData.addChild(HANDLE_BASE_RIGHT, ModelPartBuilder.create().uv(32, 0).cuboid(13F, 6F, 13F, 2F, 1F, 2F),
                ModelTransform.NONE);
        modelPartData.addChild(HANDLE_BASE_LEFT, ModelPartBuilder.create().uv(32, 0).cuboid(1F, 6F, 13F, 2F, 1F, 2F),
                ModelTransform.NONE);
        modelPartData.addChild(HANDLE_RIGHT, ModelPartBuilder.create().uv(40, 0).cuboid(-0.5F, -1F, -0.5F, 1F, 3F, 1F),
                ModelTransform.of(14F, 7F, 14F, (float) Math.PI / 8, 0, 0));
        modelPartData.addChild(HANDLE_LEFT, ModelPartBuilder.create().uv(40, 0).cuboid(-0.5F, -1F, -0.5F, 1F, 3F, 1F),
                ModelTransform.of(2F, 7F, 14F, (float) -Math.PI / 8, 0, 0));
        return TexturedModelData.of(modelData, 64, 64);
    }

    private float f(float tick) {
        final float travelRight = 40F;
        final float travelLeft = 20F;
        final float pause = 50F;

        tick %= travelRight + travelLeft + pause;
        if (tick <= travelRight) return 12F * tick / travelRight;
        if (tick <= travelRight + travelLeft) return 12F - 12F * (tick - travelRight) / travelLeft;
        else return 0;
    }

    @Override
    public void render(WeaponryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : BlockRegister.blocks[BlockRegistry.WEAPONRY.ordinal()].getDefaultState().with(
                Properties.HORIZONTAL_FACING, Direction.SOUTH);
        matrices.multiply(
                Vec3f.POSITIVE_Y.getDegreesQuaternion(-blockState.get(Properties.HORIZONTAL_FACING).asRotation()));
        matrices.translate(-0.5, -0.5, -0.5);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));

        body.render(matrices, vertexConsumer, light, overlay);

        Vec3f travel = Direction.EAST.getUnitVector();
        travel.scale(f(entity.getWorld().getTime() + tickDelta));

        armStand.translate(travel);
        armStand.render(matrices, vertexConsumer, light, overlay);
        arm.translate(travel);
        arm.render(matrices, vertexConsumer, light, overlay);
        toolLeft.translate(travel);
        toolLeft.render(matrices, vertexConsumer, light, overlay);
        toolRight.translate(travel);
        toolRight.render(matrices, vertexConsumer, light, overlay);
        armStand.resetTransform();
        arm.resetTransform();
        toolLeft.resetTransform();
        toolRight.resetTransform();


        pistolChamber.render(matrices, vertexConsumer, light, overlay);
        pistolGrip.render(matrices, vertexConsumer, light, overlay);
        handleBaseLeft.render(matrices, vertexConsumer, light, overlay);
        handleLeft.render(matrices, vertexConsumer, light, overlay);
        handleBaseRight.render(matrices, vertexConsumer, light, overlay);
        handleRight.render(matrices, vertexConsumer, light, overlay);

        matrices.pop();
    }
}
