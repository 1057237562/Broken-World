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

public class FabricatorGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 18;
    private static final int PROPERTY_COUNT = 4;

    public FabricatorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.GENERATOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 191);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar.png"),new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar_filled.png"),0,1, WBar.Direction.RIGHT);
        bar.setProperties(propertyDelegate);
        root.add(bar, 5, 2,2,1);
        for(int i = 0; i < 9; i++){
            WItemSlot itemSlot = WItemSlot.of(blockInventory, i);
            root.add(itemSlot, 1+i%3, i/3);
        }

        root.add(this.createPlayerInventoryPanel(), 0, 5);

        root.validate(this);
    }
}
