package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.entity.HyperSpearEntity;
import com.brainsmash.broken_world.entity.hostile.*;
import com.brainsmash.broken_world.entity.model.*;
import com.brainsmash.broken_world.entity.render.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import static com.brainsmash.broken_world.Main.MODID;
import static com.brainsmash.broken_world.registry.EntityModelLayerRegister.*;

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

    public static final EntityType<GlitchedZombieEntity> GLITCHED_ZOMBIE_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "glitched_zombie"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GlitchedZombieEntity::new).dimensions(
                    EntityDimensions.fixed(0.6f, 1.95f)).trackRangeBlocks(18).build());

    public static final EntityType<GlitchedSkeletonEntity> GLITCHED_SKELETON_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "glitched_skeleton"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GlitchedSkeletonEntity::new).dimensions(
                    EntityDimensions.fixed(0.6f, 1.99f)).trackRangeBlocks(24).build());

    public static final EntityType<PhoenixEntity> PHOENIX_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MODID, "phoenix"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PhoenixEntity::new).dimensions(
                    EntityDimensions.fixed(1f, 2f)).trackRangeBlocks(64).build());
    public static final EntityType<ApocalyptorEntity> APOCALYPTOR_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MODID, "apocalyptor"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ApocalyptorEntity::new).dimensions(
                    EntityDimensions.fixed(1.75f, 3f)).trackRangeBlocks(64).build());

    public static final EntityType<WerewolfEntity> WEREWOLF_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MODID, "werewolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WerewolfEntity::new).dimensions(
                    EntityDimensions.fixed(0.85f, 2.5f)).trackRangeBlocks(48).build());

    public static final EntityType<DroneEntity> DRONE_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MODID, "drone"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DroneEntity::new).dimensions(
                    EntityDimensions.fixed(0.5f, 0.5f)).trackRangeBlocks(100).build());


    @Environment(EnvType.CLIENT)
    public static void registEntitiesClientSide() {
        EntityRendererRegistry.register(BULLET_ENTITY_ENTITY_TYPE, BulletEntityRenderer::new);

        EntityRendererRegistry.register(FISHBONE_ENTITY_ENTITY_TYPE, FishboneEntityRenderer::new);
        EntityRendererRegistry.register(GLITCHED_ZOMBIE_ENTITY_TYPE, GlitchedZombieEntityRenderer::new);
        EntityRendererRegistry.register(GLITCHED_SKELETON_ENTITY_TYPE, GlitchedSkeletonEntityRenderer::new);
        EntityRendererRegistry.register(PHOENIX_ENTITY_TYPE, PhoenixEntityRenderer::new);
        EntityRendererRegistry.register(APOCALYPTOR_ENTITY_TYPE, ApocalyptorEntityRenderer::new);
        EntityRendererRegistry.register(WEREWOLF_ENTITY_TYPE, WerewolfEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_FISHBONE_LAYER, FishboneEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_PHOENIX_LAYER, PhoenixEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_APOCALYPTOR_LAYER,
                ApocalyptorEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_WEREWOLF_LAYER, WerewolfEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_DRONE_LAYER, DroneEntityModel::getTexturedModelData);
    }

    public static void registEntities() {
        FabricDefaultAttributeRegistry.register(FISHBONE_ENTITY_ENTITY_TYPE, FishboneEntity.createFishAttributes());
        FabricDefaultAttributeRegistry.register(GLITCHED_ZOMBIE_ENTITY_TYPE,
                GlitchedZombieEntity.createGlitchedZombieAttributes());
        FabricDefaultAttributeRegistry.register(GLITCHED_SKELETON_ENTITY_TYPE,
                GlitchedSkeletonEntity.createGlitchedSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(PHOENIX_ENTITY_TYPE, PhoenixEntity.createPhoenixAttributes());
        FabricDefaultAttributeRegistry.register(APOCALYPTOR_ENTITY_TYPE,
                ApocalyptorEntity.createApocalyptorAttributes());
        FabricDefaultAttributeRegistry.register(WEREWOLF_ENTITY_TYPE, WerewolfEntity.createWereworlfAttributes());
        FabricDefaultAttributeRegistry.register(DRONE_ENTITY_TYPE, DroneEntity.createLivingAttributes());
    }

    public static void registSpawnRegistration() {
        SpawnRestriction.register(APOCALYPTOR_ENTITY_TYPE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(WEREWOLF_ENTITY_TYPE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(GLITCHED_ZOMBIE_ENTITY_TYPE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(GLITCHED_SKELETON_ENTITY_TYPE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(PHOENIX_ENTITY_TYPE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PhoenixEntity::canSpawn);
    }
}
