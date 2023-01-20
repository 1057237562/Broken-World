package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.gui.util.IndicatorSlot;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.VisualLogger;
import io.github.cottonmc.cotton.gui.impl.client.NarrationMessages;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class WIndicatorItemSlot extends WWidget {
    /**
     * The default texture of item slots and {@link BackgroundPainter#SLOT}.
     *
     * @since 6.2.0
     */
    public static final Identifier SLOT_TEXTURE = new Identifier(LibGuiCommon.MOD_ID, "textures/widget/item_slot.png");

    private static final VisualLogger LOGGER = new VisualLogger(WIndicatorItemSlot.class);
    private final List<IndicatorSlot> peers = new ArrayList<>();
    @Nullable
    @Environment(EnvType.CLIENT)
    private BackgroundPainter backgroundPainter = null;
    @Nullable
    private Icon icon = null;
    private Inventory inventory;
    private int startIndex = 0;
    private int slotsWide = 1;
    private int slotsHigh = 1;
    private boolean big = false;
    private boolean insertingAllowed = true;
    private boolean takingAllowed = true;
    private int focusedSlot = -1;
    private int hoveredSlot = -1;
    private Predicate<ItemStack> filter = IndicatorSlot.DEFAULT_ITEM_FILTER;

    public WIndicatorItemSlot(Inventory inventory, int startIndex, int slotsWide, int slotsHigh, boolean big) {
        this();
        this.inventory = inventory;
        this.startIndex = startIndex;
        this.slotsWide = slotsWide;
        this.slotsHigh = slotsHigh;
        this.big = big;
        //this.ltr = ltr;
    }

    private WIndicatorItemSlot() {
        hoveredProperty().addListener((property, from, to) -> {
            assert to != null;
            if (!to) hoveredSlot = -1;
        });
    }

    public static WIndicatorItemSlot of(Inventory inventory, int index) {
        WIndicatorItemSlot w = new WIndicatorItemSlot();
        w.inventory = inventory;
        w.startIndex = index;

        return w;
    }

    public static WIndicatorItemSlot of(Inventory inventory, int startIndex, int slotsWide, int slotsHigh) {
        WIndicatorItemSlot w = new WIndicatorItemSlot();
        w.inventory = inventory;
        w.startIndex = startIndex;
        w.slotsWide = slotsWide;
        w.slotsHigh = slotsHigh;

        return w;
    }

    public static WIndicatorItemSlot outputOf(Inventory inventory, int index) {
        WIndicatorItemSlot w = new WIndicatorItemSlot();
        w.inventory = inventory;
        w.startIndex = index;
        w.big = true;

        return w;
    }

    @Override
    public int getWidth() {
        return slotsWide * 18;
    }

    @Override
    public int getHeight() {
        return slotsHigh * 18;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public boolean isBigSlot() {
        return big;
    }

    /**
     * {@return the icon if set, otherwise null}
     *
     * @since 4.1.0
     */
    @Nullable
    public Icon getIcon() {
        return this.icon;
    }

    /**
     * Sets the icon to this slot. Can be used for labeling slots for certain activities.
     *
     * @param icon the icon
     * @return this slot widget
     * @since 4.1.0
     */
    public WIndicatorItemSlot setIcon(@Nullable Icon icon) {
        this.icon = icon;

        if (icon != null && (slotsWide * slotsHigh) > 1) {
            LOGGER.warn("Setting icon {} for item slot {} with more than 1 slot ({})", icon, this, slotsWide * slotsHigh);
        }

        return this;
    }

    /**
     * Returns true if the contents of this {@code WItemSlot} can be modified by players.
     *
     * @return true if items can be inserted into or taken from this slot widget, false otherwise
     * @since 1.8.0
     */
    public boolean isModifiable() {
        return takingAllowed || insertingAllowed;
    }

    public WIndicatorItemSlot setModifiable(boolean modifiable) {
        this.insertingAllowed = modifiable;
        this.takingAllowed = modifiable;
        for (IndicatorSlot peer : peers) {
            peer.setInsertingAllowed(modifiable);
            peer.setTakingAllowed(modifiable);
        }
        return this;
    }

    /**
     * Returns whether items can be inserted into this slot.
     *
     * @return true if items can be inserted, false otherwise
     * @since 1.10.0
     */
    public boolean isInsertingAllowed() {
        return insertingAllowed;
    }

    /**
     * Sets whether inserting items into this slot is allowed.
     *
     * @param insertingAllowed true if items can be inserted, false otherwise
     * @return this slot widget
     * @since 1.10.0
     */
    public WIndicatorItemSlot setInsertingAllowed(boolean insertingAllowed) {
        this.insertingAllowed = insertingAllowed;
        for (IndicatorSlot peer : peers) {
            peer.setInsertingAllowed(insertingAllowed);
        }
        return this;
    }

    /**
     * Returns whether items can be taken from this slot.
     *
     * @return true if items can be taken, false otherwise
     * @since 1.10.0
     */
    public boolean isTakingAllowed() {
        return takingAllowed;
    }

    /**
     * Sets whether taking items from this slot is allowed.
     *
     * @param takingAllowed true if items can be taken, false otherwise
     * @return this slot widget
     * @since 1.10.0
     */
    public WIndicatorItemSlot setTakingAllowed(boolean takingAllowed) {
        this.takingAllowed = takingAllowed;
        for (IndicatorSlot peer : peers) {
            peer.setTakingAllowed(takingAllowed);
        }
        return this;
    }

    /**
     * Gets the currently focused slot index.
     *
     * @return the currently focused slot, or -1 if this widget isn't focused
     * @since 2.0.0
     */
    public int getFocusedSlot() {
        return focusedSlot;
    }

    @Override
    public void validate(GuiDescription host) {
        super.validate(host);
        peers.clear();
        int index = startIndex;

        for (int y = 0; y < slotsHigh; y++) {
            for (int x = 0; x < slotsWide; x++) {
                // The Slot object is offset +1 because it's the inner area of the slot.
                IndicatorSlot slot = createSlotPeer(inventory, index, this.getAbsoluteX() + (x * 18) + 1, this.getAbsoluteY() + (y * 18) + 1);
                slot.setInsertingAllowed(insertingAllowed);
                slot.setTakingAllowed(takingAllowed);
                slot.setFilter(filter);
                peers.add(slot);
                host.addSlotPeer(slot);
                index++;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (isActivationKey(ch) && host instanceof ScreenHandler && focusedSlot >= 0) {
            ScreenHandler handler = (ScreenHandler) host;
            MinecraftClient client = MinecraftClient.getInstance();

            IndicatorSlot peer = peers.get(focusedSlot);
            client.interactionManager.clickSlot(handler.syncId, peer.id, 0, SlotActionType.PICKUP, client.player);
        }
    }

    protected IndicatorSlot createSlotPeer(Inventory inventory, int index, int x, int y) {
        return new IndicatorSlot(inventory, index, x, y);
    }

    /**
     * Gets this slot widget's background painter.
     *
     * @return the background painter
     * @since 2.0.0
     */
    @Nullable
    @Environment(EnvType.CLIENT)
    public BackgroundPainter getBackgroundPainter() {
        return backgroundPainter;
    }

    /**
     * Sets this item slot's background painter.
     *
     * @param painter the new painter
     */
    @Environment(EnvType.CLIENT)
    public void setBackgroundPainter(@Nullable BackgroundPainter painter) {
        this.backgroundPainter = painter;
    }

    /**
     * Gets the item filter of this item slot.
     *
     * @return the item filter
     * @since 2.0.0
     */
    public Predicate<ItemStack> getFilter() {
        return filter;
    }

    /**
     * Sets the item filter of this item slot.
     *
     * @param filter the new item filter
     * @return this item slot
     * @since 2.0.0
     */
    public WIndicatorItemSlot setFilter(Predicate<ItemStack> filter) {
        this.filter = filter;
        for (IndicatorSlot peer : peers) {
            peer.setFilter(filter);
        }
        return this;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (backgroundPainter != null) {
            backgroundPainter.paintBackground(matrices, x, y, this);
        }

        if (icon != null) {
            icon.paint(matrices, x + 1, y + 1, 16);
        }
    }

    @Nullable
    @Override
    public WWidget cycleFocus(boolean lookForwards) {
        if (focusedSlot < 0) {
            focusedSlot = lookForwards ? 0 : (slotsWide * slotsHigh - 1);
            return this;
        }

        if (lookForwards) {
            focusedSlot++;
            if (focusedSlot >= slotsWide * slotsHigh) {
                focusedSlot = -1;
                return null;
            } else {
                return this;
            }
        } else {
            focusedSlot--;
            return focusedSlot >= 0 ? this : null;
        }
    }

    @Override
    public void onFocusLost() {
        focusedSlot = -1;
    }

    @Override
    public void onShown() {
        for (IndicatorSlot peer : peers) {
            peer.setVisible(true);
        }
    }

    @Override
    public InputResult onMouseMove(int x, int y) {
        int slotX = x / 18;
        int slotY = y / 18;
        hoveredSlot = slotX + slotY * slotsWide;
        return InputResult.PROCESSED;
    }

    @Override
    public void onHidden() {
        super.onHidden();

        for (IndicatorSlot peer : peers) {
            peer.setVisible(false);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addPainters() {
        backgroundPainter = BackgroundPainter.SLOT;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addNarrations(NarrationMessageBuilder builder) {
        List<Text> parts = new ArrayList<>();
        Text name = getNarrationName();
        if (name != null) parts.add(name);

        if (focusedSlot >= 0) {
            parts.add(Text.translatable(NarrationMessages.ITEM_SLOT_TITLE_KEY, focusedSlot + 1, slotsWide * slotsHigh));
        } else if (hoveredSlot >= 0) {
            parts.add(Text.translatable(NarrationMessages.ITEM_SLOT_TITLE_KEY, hoveredSlot + 1, slotsWide * slotsHigh));
        }

        builder.put(NarrationPart.TITLE, parts.toArray(new Text[0]));
    }

    /**
     * Returns a "narration name" for this slot.
     * It's narrated before the slot index. One example of a narration name would be "hotbar" for the player's hotbar.
     *
     * @return the narration name, or null if there's none for this slot
     * @since 4.2.0
     */
    @Nullable
    protected Text getNarrationName() {
        return null;
    }
}

