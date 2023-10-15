package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.items.magical.WandCore;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class WandGuiDescription extends SyncedGuiDescription {
    private final Inventory inventory;

    private final int size;

    public static WandGuiDescription createRookieWand(int syncId, PlayerInventory playerInventory) {
        return new WandGuiDescription(Main.ROOKIE_WAND_SCREEN_HANDLER, syncId, playerInventory, 1);
    }

    public static WandGuiDescription createExpertWand(int syncId, PlayerInventory playerInventory) {
        return new WandGuiDescription(Main.EXPERT_WAND_SCREEN_HANDLER, syncId, playerInventory, 3);
    }

    public static WandGuiDescription createMasterWand(int syncId, PlayerInventory playerInventory) {
        return new WandGuiDescription(Main.MASTER_WAND_SCREEN_HANDLER, syncId, playerInventory, 6);
    }

    public static WandGuiDescription createGrandMasterWand(int syncId, PlayerInventory playerInventory) {
        return new WandGuiDescription(Main.GRANDMASTER_WAND_SCREEN_HANDLER, syncId, playerInventory, 9);
    }

    public WandGuiDescription(ScreenHandlerType<WandGuiDescription> type, int syncId, PlayerInventory playerInventory, int size) {
        super(type, syncId, playerInventory);
        this.size = size;
        this.inventory = new SimpleInventory(size);
        NbtCompound nbtCompound = playerInventory.getMainHandStack().getOrCreateNbt();
        NbtList list = nbtCompound.getList("inventory", NbtElement.COMPOUND_TYPE);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                NbtElement element = list.get(i);
                inventory.setStack(i, ItemStack.fromNbt((NbtCompound) element));
            }
        }

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 130);
        root.setInsets(Insets.ROOT_PANEL);
        for (int k = 0; k < size; ++k) {
            WItemSlot slot = WItemSlot.of(inventory, k);
            slot.setFilter(itemStack -> itemStack.getItem() instanceof WandCore);
            root.add(slot, k, 1);
            slot.setLocation(slot.getX(), slot.getY() - 2);
        }
        root.add(this.createPlayerInventoryPanel(), 0, 2);
        root.validate(this);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < size ? !this.insertItem(itemStack2, size, this.slots.size(), true) : !this.insertItem(
                    itemStack2, 0, size, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        NbtList list = new NbtList();
        for (int i = 0; i < inventory.size(); i++) {
            NbtCompound itemstack = new NbtCompound();
            inventory.getStack(i).writeNbt(itemstack);
            list.add(i, itemstack);
        }
        NbtCompound nbtCompound = player.getMainHandStack().getOrCreateNbt();
        nbtCompound.put("inventory", list);
        player.getMainHandStack().setNbt(nbtCompound);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}

