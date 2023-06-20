package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.WandGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class WandScreen extends CottonInventoryScreen<WandGuiDescription> {
    public WandScreen(WandGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}

