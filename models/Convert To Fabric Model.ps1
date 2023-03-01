$files = Get-ChildItem "." -Filter *.java
foreach ($file in $files) {
    (Get-Content $file.PSPath) |
    Foreach-Object { $_ -creplace "MeshDefinition", "ModelData" } |
    Foreach-Object { $_ -creplace "meshdefinition", "modelData" } |
    Foreach-Object { $_ -creplace "PartDefinition", "ModelPartData" } |
    Foreach-Object { $_ -creplace "addOrReplaceChild", "addChild" } |
    Foreach-Object { $_ -creplace "partdefinition", "modelPartData" } |
    Foreach-Object { $_ -creplace "CubeListBuilder", "ModelPartBuilder" } |
    Foreach-Object { $_ -creplace ", new CubeDeformation\(0.0F\)", "" } |
    Foreach-Object { $_ -creplace "texOffs", "uv" } |
    Foreach-Object { $_ -creplace "addBox", "cuboid" } |
    Foreach-Object { $_ -creplace "PartPose.offsetAndRotation", "ModelTransform.of" } |
    Foreach-Object { $_ -creplace "PartPose.offset", "ModelTransform.pivot" } |
    Foreach-Object { $_ -creplace " LayerDefinition createBodyLayer", " TexturedModelData getTexturedModelData" } |
    Foreach-Object { $_ -creplace " LayerDefinition.create", " TexturedModelData.of" } |
    Set-Content $file.PSPath
}