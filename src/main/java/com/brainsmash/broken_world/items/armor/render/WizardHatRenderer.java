package com.brainsmash.broken_world.items.armor.render;

import com.brainsmash.broken_world.items.armor.render.model.WizardHatModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class WizardHatRenderer implements ArmorRenderer {
    WizardHatModel model;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (model == null) {
            model = new WizardHatModel(WizardHatModel.getModelData(Dilation.NONE, 0.0f).getRoot().createPart(128, 128));
        }

        contextModel.copyStateTo(model);
        model.head.copyTransform(contextModel.head);
        model.setVisible(false);
        model.head.visible = true;
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model,
                new Identifier("textures/models/armor/wizard_hat.png"));
    }
}
