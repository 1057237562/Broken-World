package com.brainsmash.broken_world.items.armor.material;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;

public interface ArmorMaterialWithSetBonus extends ArmorMaterial {
    void processSetBonus(LivingEntity entity);

    void processSetBonusOnPlayer(PlayerEntity player);
}
