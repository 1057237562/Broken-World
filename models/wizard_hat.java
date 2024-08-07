// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart bipedHead;
	private final ModelPart armorHead;

	public unknown(ModelPart root) {
		this.bipedHead = root.getChild("bipedHead");
		this.armorHead = root.getChild("armorHead");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData bipedHead = modelPartData.addChild("bipedHead", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData armorHead = bipedHead.addChild("armorHead", ModelPartBuilder.create().uv(53, 0).cuboid(-1.0F, -18.0F, 5.0F, 2.0F, 2.0F, 3.0F)
		.uv(69, 1).cuboid(-7.0F, -7.0F, -7.0F, 14.0F, 1.0F, 14.0F)
		.uv(0, 13).cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 3.0F, 10.0F)
		.uv(0, 0).cuboid(-4.0F, -13.0F, -3.0F, 8.0F, 3.0F, 8.0F)
		.uv(0, 27).cuboid(-3.0F, -16.0F, 1.0F, 6.0F, 4.0F, 5.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
