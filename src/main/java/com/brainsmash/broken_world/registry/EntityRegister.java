package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.entity.HyperSpearEntity;
import com.brainsmash.broken_world.entity.hostile.FishboneEntity;
import com.brainsmash.broken_world.entity.model.FishboneEntityModel;
import com.brainsmash.broken_world.entity.render.BulletEntityRenderer;
import com.brainsmash.broken_world.entity.render.FishboneEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.brainsmash.broken_world.Main.MODID;

public class EntityRegister {

    public static final EntityType<BulletEntity> BULLET_ENTITY_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MODID, "bullet"),
            FabricEntityTypeBuilder.<BulletEntity>create(SpawnGroup.MISC, BulletEntity::new).dimensions(
                    EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(6).trackedUpdateRate(20).build());
    public static final EntityType<HyperSpearEntity> HYPER_SPEAR_ENTITY_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "hyper_spear"),
            FabricEntityTypeBuilder.<HyperSpearEntity>create(SpawnGroup.MISC, HyperSpearEntity::new).dimensions(
                    EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<FishboneEntity> FISHBONE_ENTITY_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MODID, "fishbone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FishboneEntity::new).dimensions(
                    EntityDimensions.fixed(0.7f, 0.4f)).trackRangeBlocks(12).build());


    public static final EntityModelLayer MODEL_FISHBONE_LAYER = new EntityModelLayer(new Identifier(MODID, "fishbone"),
            "main");

    public static void registEntitiesClientSide() {
        EntityRendererRegistry.register(BULLET_ENTITY_ENTITY_TYPE, (context) -> new BulletEntityRenderer<>(context));

        EntityRendererRegistry.register(FISHBONE_ENTITY_ENTITY_TYPE, (context) -> new FishboneEntityRenderer(context));

        EntityModelLayerRegistry.registerModelLayer(MODEL_FISHBONE_LAYER, FishboneEntityModel::getTexturedModelData);
    }

    public static void registEntities() {
        FabricDefaultAttributeRegistry.register(FISHBONE_ENTITY_ENTITY_TYPE, FishboneEntity.createFishAttributes());
    }
}
