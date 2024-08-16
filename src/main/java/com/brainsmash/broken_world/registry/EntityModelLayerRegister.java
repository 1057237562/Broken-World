package com.brainsmash.broken_world.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static com.brainsmash.broken_world.Main.MODID;

@Environment(EnvType.CLIENT)
public class EntityModelLayerRegister {

    public static final EntityModelLayer MODEL_FISHBONE_LAYER = new EntityModelLayer(new Identifier(MODID, "fishbone"),
            "main");
    public static final EntityModelLayer MODEL_PHOENIX_LAYER = new EntityModelLayer(new Identifier(MODID, "phoenix"),
            "main");
    public static final EntityModelLayer MODEL_APOCALYPTOR_LAYER = new EntityModelLayer(
            new Identifier(MODID, "apocalyptor"), "main");

    public static final EntityModelLayer MODEL_WEREWOLF_LAYER = new EntityModelLayer(new Identifier(MODID, "werewolf"),
            "main");

    public static final EntityModelLayer MODEL_DRONE_LAYER = new EntityModelLayer(new Identifier(MODID, "drone"),
            "main");

    public static final EntityModelLayer MODEL_MAGIC_BROOM = new EntityModelLayer(new Identifier(MODID, "magic_broom"),
            "main");

    public static final EntityModelLayer MODEL_FOREST_GUARDIAN = new EntityModelLayer(
            new Identifier(MODID, "forest_guardian"), "main");

    public static final EntityModelLayer MODEL_GELOB_GEL_LAYER = new EntityModelLayer(new Identifier(MODID, "gelob_gel"), "main");
}
