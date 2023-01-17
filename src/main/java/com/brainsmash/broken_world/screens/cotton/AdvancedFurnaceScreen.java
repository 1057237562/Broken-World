package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.AdvancedFurnaceGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.CrusherGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class AdvancedFurnaceScreen extends CottonInventoryScreen<AdvancedFurnaceGuiDescription> {
    public AdvancedFurnaceScreen(AdvancedFurnaceGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
