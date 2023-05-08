// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class mutator<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "mutator"), "main");
	private final ModelPart leg;
	private final ModelPart leg2;
	private final ModelPart bb_main;

	public mutator(ModelPart root) {
		this.leg = root.getChild("leg");
		this.leg2 = root.getChild("leg2");
		this.bb_main = root.getChild("bb_main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData leg = modelPartData.addChild("leg", ModelPartBuilder.create(), ModelTransform.pivot(-6.0F, 12.0F, 0.0F));

		ModelPartData cube_r1 = leg.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 2.0F, -1.0F, 11.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

		ModelPartData cube_r2 = leg.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		ModelPartData leg2 = modelPartData.addChild("leg2", ModelPartBuilder.create(), ModelTransform.of(6.0F, 15.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r3 = leg2.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 2.0F, -1.0F, 11.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

		ModelPartData cube_r4 = leg2.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.of(-9.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -39.0F, -6.0F, 12.0F, 31.0F, 12.0F)
		.uv(0, 0).cuboid(-10.0F, -33.0F, -10.0F, 20.0F, 19.0F, 20.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
