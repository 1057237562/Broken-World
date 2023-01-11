package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.TeleporterControllerGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.TeleporterPlatformGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class TeleporterPlatformScreen extends CottonInventoryScreen<TeleporterPlatformGuiDescription> {
    public TeleporterPlatformScreen(TeleporterPlatformGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
