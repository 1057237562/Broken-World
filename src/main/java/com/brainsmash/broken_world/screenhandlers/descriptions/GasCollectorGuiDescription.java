package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.GasCollectorBlockEntity;
import com.brainsmash.broken_world.registry.GasRegister;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class GasCollectorGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 3;
    private static final int PROPERTY_COUNT = 4;
    private static final Identifier GAS_SELECTION = new Identifier(Main.MODID, "gas_selection");
    public GasCollectorGuiDescription(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.world, buf.readBlockPos()));
    }

    public GasCollectorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.GAS_COLLECTOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);

        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 1, 2, 1, 1);

        WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/progressbar_right.png"),
                new Identifier(Main.MODID, "textures/gui/progressbar_right_filled.png"), 2, 3, Direction.RIGHT);
        bar1.setProperties(propertyDelegate);
        root.add(bar1, 4, 2, 2, 1);

        WItemSlot source = WItemSlot.of(blockInventory, 0);
        root.add(source, 1, 1, 1, 1);
        WItemSlot power = WItemSlot.of(blockInventory, 1);
        root.add(power, 1, 3, 1, 1);

        context.get((world, pos) -> {
            List<Pair<Item, Integer>> gasList = GasRegister.getEnvironmentGas(world, pos);
            WButton [] gasSelectors = new WButton[3];
            for (int i = 0; i < gasSelectors.length; i++) {
                if (i < gasList.size())
                    gasSelectors[i] = new WButton(new ItemIcon(new ItemStack(gasList.get(i).getLeft())));
                else {
                    gasSelectors[i] = new WButton();
                    gasSelectors[i].setEnabled(false);
                }
                final int a = i;
                if (getNetworkSide() == NetworkSide.CLIENT) {
                    gasSelectors[i].setOnClick(() -> ScreenNetworking.of(this, NetworkSide.CLIENT).send(GAS_SELECTION, buf -> buf.writeInt(a)));
                }
                root.add(gasSelectors[i], 3+i, 2);
            }
            if (getNetworkSide() == NetworkSide.SERVER) {
                ScreenNetworking.of(this, NetworkSide.SERVER).receive(GAS_SELECTION, buf -> {
                    int i = buf.readInt();
                    if (world.getBlockEntity(pos) instanceof GasCollectorBlockEntity entity)
                        entity.selectGasOutput(i);
                });
            }
            return true;
        });

        root.add(WItemSlot.of(blockInventory, 2), 6, 2, 1, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
