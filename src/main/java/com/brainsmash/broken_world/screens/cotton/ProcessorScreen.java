package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.ProcessorGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ProcessorScreen extends CottonInventoryScreen<ProcessorGuiDescription> {
    public ProcessorScreen(ProcessorGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
