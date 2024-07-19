package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.CentrifugeGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.ColliderControllerGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ColliderControllerScreen extends CottonInventoryScreen<ColliderControllerGuiDescription> {
    public ColliderControllerScreen(ColliderControllerGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
