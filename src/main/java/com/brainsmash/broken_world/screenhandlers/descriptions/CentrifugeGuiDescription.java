package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.CentrifugeBlockEntity;
import com.brainsmash.broken_world.gui.widgets.WFluidWidget;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class CentrifugeGuiDescription extends SyncedGuiDescription {
    private static final Identifier EMPTY_TANK = new Identifier("broken_world", "empty_tank");
    private static final int INVENTORY_SIZE = 14;
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
            WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                    new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1, WBar.Direction.UP);
            bar.setProperties(propertyDelegate);
            root.add(bar, 1, 2, 1, 1);

            WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/small_pb_right.png"),
                    new Identifier(Main.MODID, "textures/gui/small_pb_right_filled.png"), 2, 3, WBar.Direction.RIGHT);
            bar1.setProperties(propertyDelegate);
            root.add(bar1, 2, 2, 1, 1);

            WItemSlot powerSlot = WItemSlot.of(blockInventory, 0);
            root.add(powerSlot, 1, 3);
            WItemSlot inputSlot = WItemSlot.of(blockInventory, 1);
            root.add(inputSlot, 1, 1);
            WItemSlot outputSlot = WItemSlot.of(blockInventory, 2, 4, 3);
            root.add(outputSlot, 3, 1);

            WFluidWidget fluidWidget = new WFluidWidget(((CentrifugeBlockEntity) world.getBlockEntity(pos)).inputInv,
                    propertyDelegate, new Identifier(Main.MODID, "textures/gui/background.png"),
                    new Identifier(Main.MODID, "textures/gui/scale.png"));
            root.add(fluidWidget, 0, 1, 1, 3);
            fluidWidget.setOnClick(integer -> {
                if (integer == 1) {
                    ScreenNetworking.of(this, NetworkSide.CLIENT).send(EMPTY_TANK, buf1 -> buf1.writeInt(0));
                    return true;
                }
                return false;
            });

            WFluidWidget fluidWidget1 = new WFluidWidget(((CentrifugeBlockEntity) world.getBlockEntity(pos)).outputInv,
                    propertyDelegate, new Identifier(Main.MODID, "textures/gui/background.png"),
                    new Identifier(Main.MODID, "textures/gui/scale.png"));
            root.add(fluidWidget1, 8, 1, 1, 3);
            fluidWidget1.setOnClick(integer -> {
                if (integer == 1) {
                    ScreenNetworking.of(this, NetworkSide.CLIENT).send(EMPTY_TANK, buf1 -> buf1.writeInt(1));
                    return true;
                }
                return false;
            });

            root.add(this.createPlayerInventoryPanel(), 0, 4);
            root.validate(this);
        }
    }

    public CentrifugeGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.CENTRIFUGE_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));
        if (getNetworkSide() == NetworkSide.SERVER) {
            ScreenNetworking.of(this, NetworkSide.SERVER).receive(EMPTY_TANK, buf -> {
                context.get((BiFunction<World, BlockPos, Object>) (world, blockPos) -> {
                    if (world.getBlockEntity(blockPos) instanceof CentrifugeBlockEntity entity) {
                        if (buf.readInt() == 0) {
                            entity.inputInv.amount = 0;
                        } else {
                            entity.outputInv.amount = 0;
                        }
                    }
                    return true;
                });
            });
            WGridPanel root = new WGridPanel();
            setRootPanel(root);
            root.setSize(150, 175);
            root.setInsets(Insets.ROOT_PANEL);
            WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                    new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1, WBar.Direction.UP);
            bar.setProperties(propertyDelegate);
            root.add(bar, 1, 2, 1, 1);

            WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/small_pb_right.png"),
                    new Identifier(Main.MODID, "textures/gui/small_pb_right_filled.png"), 2, 3, WBar.Direction.RIGHT);
            bar1.setProperties(propertyDelegate);
            root.add(bar1, 2, 2, 1, 1);

            WItemSlot powerSlot = WItemSlot.of(blockInventory, 0);
            root.add(powerSlot, 2, 3);
            WItemSlot inputSlot = WItemSlot.of(blockInventory, 1);
            root.add(inputSlot, 2, 1);
            WItemSlot outputSlot = WItemSlot.of(blockInventory, 2, 4, 3);
            root.add(outputSlot, 3, 1);

            root.add(this.createPlayerInventoryPanel(), 0, 4);

            root.validate(this);
        }
    }
}
