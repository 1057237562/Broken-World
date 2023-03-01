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
	private final ModelPart bb_main;

	public apocalyptor(ModelPart root) {
		this.body = root.getChild("body");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(30, 55).addBox(-1.0F, -42.0F, -2.0F, 3.0F, 27.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-7.0F, -22.0F, -4.0F, 14.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-8.0F, -29.0F, -5.0F, 16.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -42.0F, -6.0F, 20.0F, 13.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(74, 39).addBox(-6.0F, -17.0F, 2.1F, 13.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(74, 16).addBox(-6.0F, -17.0F, -2.1F, 13.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 77).addBox(1.0F, -17.0F, -2.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(42, 25).addBox(4.0F, -14.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 56).addBox(5.0F, -10.0F, -3.0F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(74, 44).addBox(-5.0F, -17.0F, -2.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 72).addBox(-8.0F, -14.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-10.0F, -10.0F, -3.0F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(74, 28).addBox(0.0F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 55).addBox(1.0F, -3.0F, -4.0F, 7.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(66, 56).addBox(2.0F, 11.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -14.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(60, 71).addBox(4.0F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(44, 34).addBox(1.0F, -3.0F, -4.0F, 7.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(58, 19).addBox(2.0F, 11.0F, -3.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-19.0F, -14.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition shield_r1 = bb_main.addOrReplaceChild("shield_r1", CubeListBuilder.create().texOffs(88, 45).addBox(-3.0F, -35.0F, 10.0F, 2.0F, 32.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 3.0F, -2.0742F, -1.2204F, 2.1011F));

		return LayerDefinition.create(meshdefinition, 128, 128);
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
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}