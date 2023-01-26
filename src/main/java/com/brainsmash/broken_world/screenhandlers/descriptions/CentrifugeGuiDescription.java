package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.CentrifugeBlockEntity;
import com.brainsmash.broken_world.gui.widgets.WFluidWidget;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CentrifugeGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 20;
    private static final int PROPERTY_COUNT = 4;

    public CentrifugeGuiDescription(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readBlockPos(), buf);
    }

    public CentrifugeGuiDescription(int syncId, PlayerInventory playerInventory, BlockPos pos, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.world, pos));
        if (getNetworkSide() == NetworkSide.CLIENT) {
            WGridPanel root = new WGridPanel();
            setRootPanel(root);
            root.setSize(150, 175);
            root.setInsets(Insets.ROOT_PANEL);
            WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"), new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1, WBar.Direction.DOWN);
            bar.setProperties(propertyDelegate);
            root.add(bar, 1, 2, 1, 1);

            WItemSlot powerSlot = WItemSlot.of(blockInventory, 0);
            root.add(powerSlot, 1, 3);
            WItemSlot inputSlot = WItemSlot.of(blockInventory, 1);
            root.add(inputSlot, 2, 1);
            WItemSlot outputSlot = WItemSlot.of(blockInventory, 1, 6, 3);
            root.add(outputSlot, 3, 1);

            WFluidWidget fluidWidget = new WFluidWidget(((CentrifugeBlockEntity) playerInventory.player.world.getBlockEntity(pos)).fluidInv, propertyDelegate, new Identifier(Main.MODID, "textures/gui/background.png"), new Identifier(Main.MODID, "textures/gui/scale.png"));
            root.add(fluidWidget, 0, 1, 1, 3);

            root.add(this.createPlayerInventoryPanel(), 0, 4);
            root.validate(this);
        }
    }

    public CentrifugeGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.CENTRIFUGE_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context, PROPERTY_COUNT));
        if (getNetworkSide() == NetworkSide.SERVER) {
            WGridPanel root = new WGridPanel();
            setRootPanel(root);
            root.setSize(150, 175);
            root.setInsets(Insets.ROOT_PANEL);
            WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"), new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1, WBar.Direction.RIGHT);
            bar.setProperties(propertyDelegate);
            root.add(bar, 6, 2, 2, 1);

            WItemSlot powerSlot = WItemSlot.of(blockInventory, 0);
            root.add(powerSlot, 2, 3);
            WItemSlot inputSlot = WItemSlot.of(blockInventory, 1);
            root.add(inputSlot, 2, 1);
            WItemSlot outputSlot = WItemSlot.of(blockInventory, 2, 6, 3);
            root.add(outputSlot, 3, 1);

            root.add(this.createPlayerInventoryPanel(), 0, 4);

            root.validate(this);
        }
    }
}
