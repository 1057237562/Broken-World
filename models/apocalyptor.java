// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class apocalyptor<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "apocalyptor"), "main");
	private final ModelPart body;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart right_arm;
	private final ModelPart left_arm;

	public apocalyptor(ModelPart root) {
		this.body = root.getChild("body");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(30, 55).cuboid(-2.0F, -12.0F, -2.0F, 3.0F, 27.0F, 4.0F)
		.uv(0, 42).cuboid(-8.0F, 8.0F, -4.0F, 14.0F, 5.0F, 8.0F)
		.uv(0, 25).cuboid(-9.0F, 1.0F, -5.0F, 16.0F, 7.0F, 10.0F)
		.uv(0, 0).cuboid(-11.0F, -12.0F, -6.0F, 20.0F, 13.0F, 12.0F)
		.uv(74, 39).cuboid(-7.0F, 13.0F, 2.1F, 13.0F, 5.0F, 0.0F)
		.uv(74, 16).cuboid(-7.0F, 13.0F, -2.1F, 13.0F, 5.0F, 0.0F), ModelTransform.pivot(1.0F, -6.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 77).cuboid(-3.0F, 0.0F, -2.0F, 5.0F, 3.0F, 4.0F)
		.uv(42, 25).cuboid(0.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F)
		.uv(44, 56).cuboid(1.0F, 7.0F, -3.0F, 5.0F, 10.0F, 6.0F), ModelTransform.pivot(4.0F, 7.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(74, 44).cuboid(-2.0F, 0.0F, -2.0F, 5.0F, 3.0F, 4.0F)
		.uv(44, 72).cuboid(-5.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F)
		.uv(64, 0).cuboid(-7.0F, 7.0F, -3.0F, 5.0F, 10.0F, 6.0F), ModelTransform.pivot(-3.0F, 7.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(74, 28).cuboid(10.0F, -3.0F, -3.0F, 5.0F, 5.0F, 6.0F)
		.uv(0, 55).cuboid(11.0F, -4.0F, -4.0F, 7.0F, 14.0F, 8.0F)
		.uv(66, 56).cuboid(12.0F, 10.0F, -3.0F, 5.0F, 9.0F, 6.0F), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(60, 71).cuboid(-15.0F, -3.0F, -3.0F, 5.0F, 5.0F, 6.0F)
		.uv(44, 34).cuboid(-18.0F, -4.0F, -4.0F, 7.0F, 14.0F, 8.0F)
		.uv(58, 19).cuboid(-17.0F, 10.0F, -3.0F, 5.0F, 9.0F, 6.0F), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

		ModelPartData shield_r1 = left_arm.addChild("shield_r1", ModelPartBuilder.create().uv(88, 45).cuboid(-3.0F, -35.0F, 10.0F, 2.0F, 32.0F, 10.0F), ModelTransform.of(-4.0F, 37.0F, 3.0F, 2.3521F, -1.3334F, -2.285F));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
