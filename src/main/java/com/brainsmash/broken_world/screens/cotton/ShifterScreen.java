package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.CrusherGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.ShifterGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ShifterScreen extends CottonInventoryScreen<ShifterGuiDescription> {
    public ShifterScreen(ShifterGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
