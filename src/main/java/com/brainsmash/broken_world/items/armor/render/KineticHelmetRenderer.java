package com.brainsmash.broken_world.items.armor.render;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KineticHelmetRenderer implements ArmorRenderer {

    BipedEntityModel<LivingEntity> armorModel;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (armorModel == null) {
            armorModel = new BipedEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(
                    EntityModelLayers.PLAYER_OUTER_ARMOR));
        }
        contextModel.copyStateTo(armorModel);
        armorModel.head.copyTransform(contextModel.head);
        armorModel.setVisible(false);
        armorModel.head.visible = true;
        renderPart(matrices, vertexConsumers, light, stack, armorModel,
                getArmorTexture((ArmorItem) stack.getItem(), false, null));
    }

    private Identifier getArmorTexture(ArmorItem item, boolean secondLayer, @Nullable String overlay) {
        String material = item.getMaterial().getName();
        String string = "textures/models/armor/" + material + "_layer_" + (secondLayer ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
        return new Identifier(string);
    }

    private void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, Model model, Identifier texture) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers,
                RenderLayer.getEntityTranslucent(texture), false, stack.hasGlint());
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }
}
