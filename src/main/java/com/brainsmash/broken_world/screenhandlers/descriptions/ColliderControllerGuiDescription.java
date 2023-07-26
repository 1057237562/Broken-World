package com.brainsmash.broken_world.screenhandlers.descriptions;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;

public class ColliderControllerGuiDescription extends SyncedGuiDescription {

    public ColliderControllerGuiDescription(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory) {
        super(type, syncId, playerInventory);
    }
}
