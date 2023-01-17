// Made with Blockbench 4.6.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class custom_model<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "custom_model"), "main");
	private final ModelPart bb_main;

	public custom_model(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition blade3_r1 = bb_main.addOrReplaceChild("blade3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -144.0F, -1.0F, 12.0F, 144.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 2.0944F));

		PartDefinition blade2_r1 = bb_main.addOrReplaceChild("blade2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -144.0F, -1.0F, 12.0F, 144.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, -2.0944F));

		PartDefinition blade1_r1 = bb_main.addOrReplaceChild("blade1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -144.0F, -1.0F, 12.0F, 144.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}