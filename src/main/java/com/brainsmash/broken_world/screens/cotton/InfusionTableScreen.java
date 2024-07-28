package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.InfusionTableGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.client.search.SearchManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class InfusionTableScreen extends CottonInventoryScreen<InfusionTableGuiDescription> {
    protected SearchManager searchManager = new SearchManager();

    public InfusionTableScreen(InfusionTableGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
