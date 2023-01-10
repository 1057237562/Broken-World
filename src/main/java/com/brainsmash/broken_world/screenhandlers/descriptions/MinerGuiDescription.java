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

public class MinerGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 25;
    private static final int PROPERTY_COUNT = 4;

    public MinerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.MINER_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID,"textures/gui/small_electric_bar.png"),new Identifier(Main.MODID,"textures/gui/small_electric_bar_filled.png"),0,1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 8, 2,1,1);
        for(int i = 0;i<3;i++){
            for(int j = 0;j<8;j++){
                WItemSlot itemSlot = WItemSlot.of(blockInventory,7*i+j+1);
                root.add(itemSlot,j,i+1,1,1);
            }
        }

        WItemSlot power = WItemSlot.of(blockInventory,0);
        root.add(power,8,3,1,1);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
