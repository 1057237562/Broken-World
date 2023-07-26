package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ColliderControllerGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 4;
    private static final int PROPERTY_COUNT = 4;
    public ColliderControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.COLLIDER_CONTROLLER_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 8, 2, 1, 1);
        WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/progressbar.png"),
                new Identifier(Main.MODID, "textures/gui/progressbar_filled.png"), 2, 3, WBar.Direction.LEFT);
        bar1.setProperties(propertyDelegate);
        root.add(bar1, 5, 1, 1, 1);

        WItemSlot s1 = WItemSlot.of(blockInventory, 0);
        root.add(s1, 2, 1, 1, 1);
        WItemSlot s2 = WItemSlot.of(blockInventory, 1);
        root.add(s2, 6, 1, 1, 1);
        WItemSlot out = WItemSlot.of(blockInventory, 2);
        root.add(out, 4, 1, 1, 1);
        WItemSlot power = WItemSlot.of(blockInventory, 3);
        root.add(power, 8, 3, 1, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
