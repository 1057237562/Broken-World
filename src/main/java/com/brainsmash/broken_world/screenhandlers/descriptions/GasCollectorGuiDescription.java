package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.GasCollectorBlockEntity;
import com.brainsmash.broken_world.gui.widgets.WTintedBar;
import com.brainsmash.broken_world.registry.GasRegister;
import com.brainsmash.broken_world.util.Reference;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WBar.Direction;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;

public class GasCollectorGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 3;
    private static final int PROPERTY_COUNT = 4;
    private static final Identifier GAS_SELECTION = new Identifier(Main.MODID, "gas_selection");
    private static final int TRACE_THRESHOLD = 80;
    private static final int NORMAL_THRESHOLD = 40;
    private static final Texture TRACE_BG = new Texture(new Identifier(Main.MODID, "textures/gui/trace_gas.png"));
    private static final Texture TRACE_FG = new Texture(new Identifier(Main.MODID, "textures/gui/trace_gas_filled.png"));
    private static final Texture NORMAL_BG = new Texture(new Identifier(Main.MODID, "textures/gui/normal_gas.png"));
    private static final Texture NORMAL_FG = new Texture(new Identifier(Main.MODID, "textures/gui/normal_gas_filled.png"));
    private static final Texture ABUNDANT_BG = new Texture(new Identifier(Main.MODID, "textures/gui/abundant_gas.png"));
    private static final Texture ABUNDANT_FG = new Texture(new Identifier(Main.MODID, "textures/gui/abundant_gas_filled.png"));

    final Reference<Integer> selectedGas = new Reference<>(0);
    public GasCollectorGuiDescription(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.world, buf.readBlockPos()), buf.readInt());
    }

    public GasCollectorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, int selectedGas) {
        super(Main.GAS_COLLECTOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));

        this.selectedGas.value = selectedGas;

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 211);
        root.setInsets(Insets.ROOT_PANEL);

        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 1, 3, 1, 1);

        WTintedBar bar1 = new WTintedBar((Texture) null, (Texture) null, 2, 3, Direction.DOWN);
        bar1.setProperties(propertyDelegate);
        root.add(bar1, 4, 2, 1, 3);

        WItemSlot source = WItemSlot.of(blockInventory, 0);
        root.add(source, 4, 1, 1, 1);
        WItemSlot power = WItemSlot.of(blockInventory, 1);
        root.add(power, 1, 4, 1, 1);
        WItemSlot product = WItemSlot.of(blockInventory, 2);
        root.add(product, 4, 5, 1, 1);

        context.get((world, pos) -> {
            List<Pair<GasRegister.Gas, Integer>> gasList = GasRegister.getBiomeGases(world, pos);
            final WButton [] gasSelectors = new WButton[gasList.size()];
            final int l = gasSelectors.length;
            for (int i = 0; i < l; i++) {
                gasSelectors[i] = new WButton(new ItemIcon(new ItemStack(gasList.get(i).getLeft().product())));

                final int j = i;
                if (getNetworkSide() == NetworkSide.CLIENT) {
                    gasSelectors[i].setOnClick(() -> {
                        this.selectedGas.value = (l + this.selectedGas.value - l/2 + j) % l;
                        updateButtons(gasSelectors, gasList);
                        updateBarColor(bar1, gasList);
                        ScreenNetworking.of(this, NetworkSide.CLIENT).send(GAS_SELECTION, buf -> buf.writeInt(this.selectedGas.value));
                    });
                }
                root.add(gasSelectors[i], 4 - l / 2 + i, 3);
            }
            updateButtons(gasSelectors, gasList);
            updateBarColor(bar1, gasList);
            if (getNetworkSide() == NetworkSide.SERVER) {
                ScreenNetworking.of(this, NetworkSide.SERVER).receive(GAS_SELECTION, buf -> {
                    int i = buf.readInt();
                    if (world.getBlockEntity(pos) instanceof GasCollectorBlockEntity entity)
                        entity.selectGasOutput(i);
                });
            }
            return true;
        });

        root.add(this.createPlayerInventoryPanel(), 0, 6);

        root.validate(this);
    }

    void updateButtons(WButton [] buttons, List<Pair<GasRegister.Gas, Integer>> gasList) {
        int l = buttons.length;
        for (int i = 0; i < l; i++) {
            buttons[(l/2 + i) % l].setIcon(new ItemIcon(gasList.get((selectedGas.value + i) % l).getLeft().product()));
        }
    }

    void updateBarColor(WTintedBar bar, List<Pair<GasRegister.Gas, Integer>> gasList) {
        int color;
        int ticks;
        if (!gasList.isEmpty()) {
            color = 0xFF_FFFFFF;
            ticks = 40;
        } else {
            Pair<GasRegister.Gas, Integer> pair = gasList.get(selectedGas.value);
            color = pair.getLeft().color();
            ticks = pair.getRight();
        }
        if (ticks >= TRACE_THRESHOLD) {
            bar.setBG(TRACE_BG);
            bar.setBar(TRACE_FG);
        } else if (ticks >= NORMAL_THRESHOLD) {
            bar.setBG(NORMAL_BG);
            bar.setBar(NORMAL_FG);
        } else {
            bar.setBG(ABUNDANT_BG);
            bar.setBar(ABUNDANT_FG);
        }

        bar.setTint(color);
    }
}
