package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderControllerBlockEntity;
import com.brainsmash.broken_world.gui.widgets.WColliderStatus;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class ColliderControllerGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 4;


    public ColliderControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.COLLIDER_CONTROLLER_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, ColliderControllerBlockEntity.PROPERTY_COUNT));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 193);
        root.setInsets(Insets.ROOT_PANEL);

        WBar electricBar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
        root.add(electricBar, 8, 3, 1, 1);
        WItemSlot power = WItemSlot.of(blockInventory, 3);
        root.add(power, 8, 4, 1, 1);

        WBar barL = new WBar(new Identifier(Main.MODID, "textures/gui/collider/progressbar.png"),
                new Identifier(Main.MODID, "textures/gui/collider/progressbar_filled.png"), 2, 3, WBar.Direction.RIGHT);
        root.add(barL, 2, 2, 2, 1);
        WBar barR = new WBar(new Identifier(Main.MODID, "textures/gui/collider/progressbar.png"),
                new Identifier(Main.MODID, "textures/gui/collider/progressbar_filled.png"), 2, 3, WBar.Direction.LEFT);
        root.add(barL, 2, 2, 2, 1);
        root.add(barR, 5, 2, 2, 1);

        WColliderStatus status = new WColliderStatus(4);
        root.add(status, 4, 2, 1, 1);

        WItemSlot s1 = WItemSlot.of(blockInventory, 0);
        root.add(s1, 1, 2, 1, 1);
        WItemSlot s2 = WItemSlot.of(blockInventory, 1);
        root.add(s2, 7, 2, 1, 1);
        WItemSlot out = WItemSlot.of(blockInventory, 2);
        out.setInsertingAllowed(false);
        root.add(out, 4, 2, 1, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 5);

        root.validate(this);
    }
}
