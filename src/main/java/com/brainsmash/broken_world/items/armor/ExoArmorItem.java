package com.brainsmash.broken_world.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

import java.util.UUID;

public class ExoArmorItem extends ArmorItem {

    public static final UUID MOVEMENT_SPEED_MODIFIER_ID = UUID.fromString("2f5f1a2d-3f5d-4f1a-8c3d-5f1a2d3f5d4f");
    public static final UUID ARMOR_DAMAGE_MODIFIER_ID = UUID.fromString("2f5f1a2d-3f5d-4f1a-8c3d-5f1a2d3f5d4d");
    public static final UUID ARMOR_KNOCKBACK_MODIFIER_ID = UUID.fromString("2f5f1a2d-3f5d-4f1a-8c3d-5f1a2d3f5d4e");


    public ExoArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (getSlotType().equals(slot)) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(MOVEMENT_SPEED_MODIFIER_ID, "Armor speed bonus", 0.05,
                            EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ARMOR_DAMAGE_MODIFIER_ID, "Armor damage bonus", 0.25,
                            EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
                    new EntityAttributeModifier(ARMOR_KNOCKBACK_MODIFIER_ID, "Armor knockback bonus", 0.1,
                            EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        } else {
            return super.getAttributeModifiers(slot);
        }
    }
}
