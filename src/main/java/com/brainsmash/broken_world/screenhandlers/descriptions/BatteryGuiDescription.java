package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class BatteryGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 1;
    private static final int PROPERTY_COUNT = 2;

    public BatteryGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.BATTERY_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/vertical_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/vertical_electric_bar_filled.png"), 0, 1);
        bar.setProperties(propertyDelegate).withTooltip("%s/%s IU");
        root.add(bar, 4, 1, 1, 2);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }

    public int getEnergy() {
        return propertyDelegate.get(0);
    }
}
