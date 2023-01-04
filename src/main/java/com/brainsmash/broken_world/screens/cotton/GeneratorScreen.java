package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.GeneratorGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class GeneratorScreen extends CottonInventoryScreen<GeneratorGuiDescription> {
    public GeneratorScreen(GeneratorGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
