// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class centrifuge_inner_Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "centrifuge_inner_converted"), "main");
	private final ModelPart bone;

	public centrifuge_inner_Converted(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -13.0F, 7.0F, 2.0F, 8.0F, 2.0F)
		.uv(8, 0).cuboid(-9.0F, -16.0F, 7.0F, 2.0F, 14.0F, 2.0F)
		.uv(0, 0).cuboid(-14.0F, -13.0F, 7.0F, 2.0F, 8.0F, 2.0F)
		.uv(0, 0).cuboid(-9.0F, -13.0F, 12.0F, 2.0F, 8.0F, 2.0F)
		.uv(0, 0).cuboid(-9.0F, -13.0F, 2.0F, 2.0F, 8.0F, 2.0F)
		.uv(2, 4).cuboid(-9.0F, -11.0F, 2.0F, 2.0F, 0.0F, 12.0F)
		.uv(-2, 16).cuboid(-14.0F, -11.0F, 7.0F, 12.0F, 0.0F, 2.0F), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

		return TexturedModelData.of(modelData, 24, 18);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
