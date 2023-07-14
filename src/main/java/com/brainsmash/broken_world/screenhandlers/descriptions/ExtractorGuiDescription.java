package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class ExtractorGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 3;
    private static final int PROPERTY_COUNT = 4;

    public ExtractorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.EXTRACTOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 3, 2, 1, 1);
        WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/progressbar_right.png"),
                new Identifier(Main.MODID, "textures/gui/progressbar_right_filled.png"), 2, 3, Direction.RIGHT);
        bar1.setProperties(propertyDelegate);
        root.add(bar1, 4, 2, 2, 1);

        WItemSlot source = WItemSlot.of(blockInventory, 0);
        root.add(source, 3, 1, 1, 1);
        WItemSlot power = WItemSlot.of(blockInventory, 1);
        root.add(power, 3, 3, 1, 1);

        root.add(WItemSlot.of(blockInventory, 2), 6, 2, 1, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
