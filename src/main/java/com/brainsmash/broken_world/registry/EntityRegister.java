package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.entities.DetectEntity;
import com.brainsmash.broken_world.entities.model.DetectEntityModel;
import com.brainsmash.broken_world.entities.render.DetectEntityRenderer;
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

    public static final EntityType<DetectEntity> DETECTOR = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MODID, "detector"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DetectEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build()
    );

    public static final EntityModelLayer MODEL_DETECTOR_LAYER = new EntityModelLayer(new Identifier(MODID, "detector"), "main");


    public static void RegistEntitiesClientSide(){
        EntityRendererRegistry.register(DETECTOR, (context) -> new DetectEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(MODEL_DETECTOR_LAYER, DetectEntityModel::getTexturedModelData);
    }

    public static void RegistEntities(){
        FabricDefaultAttributeRegistry.register(DETECTOR, DetectEntity.createMobAttributes());
    }
}
