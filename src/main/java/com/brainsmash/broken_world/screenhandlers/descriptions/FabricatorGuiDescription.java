package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.gui.util.IndicatorSlot;
import com.brainsmash.broken_world.gui.widgets.WIndicatorItemSlot;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Set;

public class FabricatorGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 18;
    private static final int PROPERTY_COUNT = 4;

    public int indicator = 0;
    public Slot lslot;

    public FabricatorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.FABRICATOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 207);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar.png"),new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar_filled.png"),0,1, WBar.Direction.RIGHT);
        bar.setProperties(propertyDelegate);
        root.add(bar, 5, 2,2,1);
        WIndicatorItemSlot craftingSlot = new WIndicatorItemSlot(blockInventory,0,3,3,false);
        root.add(craftingSlot,1,1);
        WItemSlot storageSlot = new WItemSlot(blockInventory,9,9,1,false);
        root.add(storageSlot,0,5);
        /*for(int i = 8; i >= 0; i--){
            WIndicatorItemSlot itemSlot = WIndicatorItemSlot.of(blockInventory, i);
            root.add(itemSlot, 1+i%3, i/3+1);
        }*/

        root.add(this.createPlayerInventoryPanel(), 0, 6);

        root.validate(this);
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        if(slot.getStack().isEmpty() && slot instanceof IndicatorSlot) {
            indicator++;
            if (indicator == 1) {
                lslot = slot;
            }
            MinecraftClient client = MinecraftClient.getInstance();
            client.interactionManager.clickSlot(syncId, lslot.id, 0, SlotActionType.PICKUP, client.player);
            client.interactionManager.clickSlot(syncId, slot.id, 0, SlotActionType.PICKUP, client.player);
            return true;
        }else{
            return super.canInsertIntoSlot(slot);
        }
    }
}