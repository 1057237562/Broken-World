package com.brainsmash.broken_world.items.magical;

import com.brainsmash.broken_world.items.magical.enums.RuneEnum;
import net.minecraft.item.Item;

public abstract class Rune extends Item {

    final RuneEnum type;

    public Rune(Settings settings, RuneEnum type) {
        super(settings);
        this.type = type;
    }
}
