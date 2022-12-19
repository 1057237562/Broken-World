package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.BiConsumer;

public class TeleporterControllerGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 1;
    private static String selectDim;
    public TeleporterControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        selectDim = "none";
        BiConsumer<String,WButton> buttonBiConsumer = (s, wButton) -> {
            wButton.setLabel(Text.of(s));
            wButton.setOnClick(() -> {selectDim = s;});
        };
        WListPanel<String,WButton> dimList = new WListPanel<>(List.of("broken_world:moon","broken_world:metallic"), () -> {
            return new WButton(Text.of(""));
        }, buttonBiConsumer);
        root.add(dimList,0,1,8,3);
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 8, 2);
        WButton select = new WButton(Text.of("√"));
        select.setOnClick(() -> {System.out.println(selectDim);});
        root.add(select,8,3);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
