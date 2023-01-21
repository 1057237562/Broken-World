package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.gui.widgets.WIndicatorItemSlot;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class FabricatorGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 18;
    private static final int PROPERTY_COUNT = 4;

    public FabricatorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.FABRICATOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 191);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar.png"),new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar_filled.png"),0,1, WBar.Direction.RIGHT);
        bar.setProperties(propertyDelegate);
        root.add(bar, 5, 2,2,1);
        WIndicatorItemSlot craftingSlot = new WIndicatorItemSlot(blockInventory,0,3,3,false);
        root.add(craftingSlot,1,1);
        /*for(int i = 8; i >= 0; i--){
            WIndicatorItemSlot itemSlot = WIndicatorItemSlot.of(blockInventory, i);
            root.add(itemSlot, 1+i%3, i/3+1);
        }*/

        root.add(this.createPlayerInventoryPanel(), 0, 5);

        root.validate(this);
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        if(slot.getStack().isEmpty()) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.interactionManager.clickSlot(syncId, slot.id, 0, SlotActionType.PICKUP, client.player);
        }
        return false;
    }
}