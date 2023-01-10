package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.MinerGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.ShifterGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class MinerScreen extends CottonInventoryScreen<MinerGuiDescription> {
    public MinerScreen(MinerGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }
}
