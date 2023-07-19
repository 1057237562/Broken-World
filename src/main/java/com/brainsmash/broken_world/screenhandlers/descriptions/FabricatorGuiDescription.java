package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.FabricatorBlockEntity;
import com.brainsmash.broken_world.gui.util.IndicatorSlot;
import com.brainsmash.broken_world.gui.widgets.WIndicatorItemSlot;
import com.brainsmash.broken_world.recipe.FabricatorRecipe;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class FabricatorGuiDescription extends SyncedGuiDescription {
    private static final Identifier RECIPE_CHANGED = new Identifier("broken_world", "recipe_changed");
    private static final int INVENTORY_SIZE = 19;
    private static final int PROPERTY_COUNT = 4;

    public int indicator = 0;
    public Slot lslot;
    private FabricatorBlockEntity entity;

    public FabricatorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.FABRICATOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context, PROPERTY_COUNT));
        context.get((world, pos) -> {
            if (world.getBlockEntity(pos) instanceof FabricatorBlockEntity fabricatorBlockEntity) {
                entity = fabricatorBlockEntity;
            }
            return true;
        });

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 207);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/horizontal_electric_bar.png"), new Identifier(Main.MODID, "textures/gui/horizontal_electric_bar_filled.png"), 0, 1, WBar.Direction.RIGHT);
        bar.setProperties(propertyDelegate);
        root.add(bar, 5, 1, 2, 1);
        WItemSlot output = new WItemSlot(blockInventory, 18, 1, 1, false);
        root.add(output, 6, 3);
        WIndicatorItemSlot craftingSlot = new WIndicatorItemSlot(blockInventory, 0, 3, 3, false);
        craftingSlot.addChangeListener((WIndicatorItemSlot.ChangeListener) (slot, inventory, index, stack) -> {
            if (getNetworkSide() == NetworkSide.SERVER) {
                checkRecipe();
            }
        });
        root.add(craftingSlot, 1, 1);
        WBar bar1 = new WBar(new Identifier(Main.MODID, "textures/gui/progressbar_right.png"), new Identifier(Main.MODID, "textures/gui/progressbar_right_filled.png"), 2, 3, WBar.Direction.RIGHT);
        root.add(bar1, 4, 3, 2, 1);
        WItemSlot storageSlot = new WItemSlot(blockInventory, 9, 9, 1, false);
        root.add(storageSlot, 0, 5);

        root.add(this.createPlayerInventoryPanel(), 0, 6);

        root.validate(this);
    }

    public void checkRecipe() {
        if (getNetworkSide() == NetworkSide.SERVER) {
            CraftingInventory craftingInventory = new CraftingInventory(this, 3, 3);
            for (int i = 0; i < 9; i++) {
                craftingInventory.setStack(i, blockInventory.getStack(i));
            }
            Optional<FabricatorRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(FabricatorRecipe.Type.INSTANCE, craftingInventory, world);
            if (optional.isPresent()) {
                entity.setOutput(optional.get().craft(craftingInventory));
            } else {
                entity.setOutput(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        if (slot.getStack().isEmpty() && slot instanceof IndicatorSlot) {
            if (!slot.equals(lslot)) indicator++;
            if (indicator == 1) {
                lslot = slot;
            }
            MinecraftClient client = MinecraftClient.getInstance();
            if (indicator == 1)
                client.interactionManager.clickSlot(syncId, slot.id, 0, SlotActionType.PICKUP, client.player);
            client.interactionManager.clickSlot(syncId, slot.id, 0, SlotActionType.PICKUP, client.player);
            return true;
        } else {
            return super.canInsertIntoSlot(slot);
        }
    }
}