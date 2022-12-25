package com.brainsmash.broken_world.screens.cotton;

import com.brainsmash.broken_world.screenhandlers.descriptions.BatteryGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.TeleporterControllerGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BatteryScreen extends CottonInventoryScreen<BatteryGuiDescription> {
    public BatteryScreen(BatteryGuiDescription description, PlayerInventory playerInventory, Text title) {
        super(description, playerInventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrices, partialTicks, mouseX, mouseY);
    }
}
