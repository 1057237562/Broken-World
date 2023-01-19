package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.FabricatorGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.GeneratorGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class FabricatorScreen  extends CottonInventoryScreen<FabricatorGuiDescription> {
    public FabricatorScreen(FabricatorGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
