package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class GeneratorGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 1;
    private static final int PROPERTY_COUNT = 4;

    public GeneratorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.GENERATOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/horizontal_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/horizontal_electric_bar_filled.png"), 0, 1,
                WBar.Direction.RIGHT);
        bar.setProperties(propertyDelegate).withTooltip("%s/%s IU");
        root.add(bar, 5, 2, 2, 1);
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 3, 2);
        WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/fuel_slot.png"),
                new Identifier(Main.MODID, "textures/gui/fuel_slot_filled.png"), 2, 3);
        root.add(bar1, 3, 1, 1, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
