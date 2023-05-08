// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class remote_drone<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "remote_drone"), "main");
	private final ModelPart bb_main;

	public remote_drone(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 25).cuboid(-3.0F, -5.0F, -3.0F, 6.0F, 4.0F, 6.0F)
		.uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 3.0F, 8.0F)
		.uv(24, 25).cuboid(-2.0F, -11.0F, -2.0F, 4.0F, 3.0F, 4.0F)
		.uv(0, 11).cuboid(-1.0F, -16.0F, 3.0F, 1.0F, 8.0F, 1.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(0, 11).cuboid(-1.0F, -1.0F, -9.0F, 3.0F, 2.0F, 10.0F), ModelTransform.of(0.0F, -3.0F, 0.0F, -2.9671F, 0.0F, 3.1416F));

		ModelPartData cube_r2 = bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(16, 13).cuboid(-1.0F, -1.0F, -9.0F, 3.0F, 2.0F, 10.0F), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.1745F, 1.0472F, 0.0F));

		ModelPartData cube_r3 = bb_main.addChild("cube_r3", ModelPartBuilder.create().uv(22, 1).cuboid(-1.0F, -1.0F, -9.0F, 3.0F, 2.0F, 10.0F), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.1745F, -1.0472F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
