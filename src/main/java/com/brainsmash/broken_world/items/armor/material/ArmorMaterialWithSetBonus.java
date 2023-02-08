package com.brainsmash.broken_world.items.armor.material;

import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import net.minecraft.item.ArmorMaterial;

public interface ArmorMaterialWithSetBonus extends ArmorMaterial {
    void processSetBonus(EntityDataExtension dataExtension);

    void reverseSetBonus(EntityDataExtension dataExtension);
}
