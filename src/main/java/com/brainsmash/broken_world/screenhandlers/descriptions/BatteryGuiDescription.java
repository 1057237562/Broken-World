package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.BatteryBlockEntity;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;

public class BatteryGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 1;
    public BatteryGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.BATTERY_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WLabel label = new WLabel(Text.of("Current Energy:"));
        root.add(label, 0, 2);
        context.run((world, blockPos) -> {
            if(world.getBlockEntity(blockPos) instanceof BatteryBlockEntity battery){
                label.setText(Text.of(String.valueOf(battery.getEnergy())));
            }
        });


        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
