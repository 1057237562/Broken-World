// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class phoenix<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "phoenix"), "main");
	private final ModelPart tail;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart head;
	private final ModelPart bb_main;

	public phoenix(ModelPart root) {
		this.tail = root.getChild("tail");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.head = root.getChild("head");
		this.bb_main = root.getChild("bb_main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, 14.0F, 6.0F));

		ModelPartData cube_r1 = tail.addChild("cube_r1", ModelPartBuilder.create().uv(34, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1381F, -0.3875F, -0.081F));

		ModelPartData cube_r2 = tail.addChild("cube_r2", ModelPartBuilder.create().uv(12, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(2.0F, 2.0F, 0.0F, 0.192F, 0.2161F, -0.0271F));

		ModelPartData cube_r3 = tail.addChild("cube_r3", ModelPartBuilder.create().uv(16, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(2.0F, 2.0F, 0.0F, 0.0275F, 0.2161F, -0.0271F));

		ModelPartData cube_r4 = tail.addChild("cube_r4", ModelPartBuilder.create().uv(4, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(2.0F, 2.0F, 0.0F, 0.192F, -0.002F, -0.0265F));

		ModelPartData cube_r5 = tail.addChild("cube_r5", ModelPartBuilder.create().uv(20, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(1.0F, 2.0F, 0.0F, 0.0334F, -0.002F, -0.0265F));

		ModelPartData cube_r6 = tail.addChild("cube_r6", ModelPartBuilder.create().uv(8, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(1.0F, 2.0F, 0.0F, 0.192F, -0.2637F, -0.0274F));

		ModelPartData cube_r7 = tail.addChild("cube_r7", ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(1.0F, 2.0F, 0.0F, 0.0405F, -0.2637F, -0.0274F));

		ModelPartData cube_r8 = tail.addChild("cube_r8", ModelPartBuilder.create().uv(34, 21).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 21.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1322F, -0.2578F, -0.0624F));

		ModelPartData cube_r9 = tail.addChild("cube_r9", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -1.0F, 3.0F, 1.0F, 49.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.128F, -0.0352F, -0.0126F));

		ModelPartData cube_r10 = tail.addChild("cube_r10", ModelPartBuilder.create().uv(0, 50).cuboid(0.0F, -2.0F, -1.0F, 3.0F, 1.0F, 49.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1283F, 0.0F, -0.0171F));

		ModelPartData cube_r11 = tail.addChild("cube_r11", ModelPartBuilder.create().uv(55, 1).cuboid(-2.0F, -2.0F, -1.0F, 3.0F, 1.0F, 49.0F), ModelTransform.of(3.0F, 2.0F, 0.0F, 0.1283F, 0.0698F, -0.0171F));

		ModelPartData cube_r12 = tail.addChild("cube_r12", ModelPartBuilder.create().uv(0, 29).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 20.0F), ModelTransform.of(3.0F, 2.0F, 0.0F, 0.134F, 0.3047F, 0.0119F));

		ModelPartData cube_r13 = tail.addChild("cube_r13", ModelPartBuilder.create().uv(4, 29).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 20.0F), ModelTransform.of(3.0F, 2.0F, 0.0F, 0.1409F, 0.4343F, 0.0311F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(4, 10).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 8.0F, 1.0F)
		.uv(0, 32).cuboid(-2.0F, 7.0F, -4.0F, 3.0F, 0.0F, 3.0F), ModelTransform.pivot(2.0F, 17.0F, 4.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 10).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 8.0F, 1.0F)
		.uv(0, 32).cuboid(-1.0F, 7.0F, -4.0F, 3.0F, 0.0F, 3.0F), ModelTransform.pivot(-3.0F, 17.0F, 4.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(109, 98).cuboid(-2.0F, -4.0F, -1.0F, 3.0F, 3.0F, 1.0F)
		.uv(12, 3).cuboid(-2.0F, -1.0F, -3.0F, 3.0F, 1.0F, 1.0F)
		.uv(9, 14).cuboid(-1.0F, 0.0F, -6.0F, 1.0F, 2.0F, 5.0F)
		.uv(9, 0).cuboid(-2.0F, 0.0F, -4.0F, 3.0F, 1.0F, 2.0F)
		.uv(9, 7).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 4.0F, 3.0F), ModelTransform.pivot(0.0F, 3.0F, -6.0F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData right_wing_r1 = bb_main.addChild("right_wing_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 6.0F, 23.0F)
		.uv(0, 50).cuboid(5.0F, -1.0F, -1.0F, 1.0F, 6.0F, 23.0F), ModelTransform.of(-3.0F, -13.0F, -3.0F, -0.4363F, 0.0F, 0.0F));

		ModelPartData neck_r1 = bb_main.addChild("neck_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -3.0F, 3.0F, 7.0F, 3.0F), ModelTransform.of(0.0F, -16.0F, -3.0F, 0.3927F, 0.0F, 0.0F));

		ModelPartData body_r1 = bb_main.addChild("body_r1", ModelPartBuilder.create().uv(55, 31).cuboid(-3.0F, -8.0F, -8.0F, 5.0F, 4.0F, 11.0F), ModelTransform.of(0.0F, -4.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
