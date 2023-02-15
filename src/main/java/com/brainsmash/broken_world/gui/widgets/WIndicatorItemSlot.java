package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.gui.util.IndicatorSlot;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.VisualLogger;
import io.github.cottonmc.cotton.gui.impl.client.NarrationMessages;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class WIndicatorItemSlot extends WItemSlot {
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
    private final Inventory inventory;
    private final int startIndex;
    private final int slotsWide;
    private final int slotsHigh;
    private final boolean big;
    private int focusedSlot = -1;
    private Predicate<ItemStack> filter = IndicatorSlot.DEFAULT_ITEM_FILTER;
    private final Set<ChangeListener> listeners = new HashSet<>();

    public WIndicatorItemSlot(Inventory inventory, int startIndex, int slotsWide, int slotsHigh, boolean big) {
        super(inventory, startIndex, slotsWide, slotsHigh, big);
        this.inventory = inventory;
        this.startIndex = startIndex;
        this.slotsWide = slotsWide;
        this.slotsHigh = slotsHigh;
        this.big = big;
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
        peers.clear();
        int index = startIndex;

        for (int y = 0; y < slotsHigh; y++) {
            for (int x = 0; x < slotsWide; x++) {
                // The Slot object is offset +1 because it's the inner area of the slot.
                IndicatorSlot slot = createSlotPeer(inventory, index, this.getAbsoluteX() + (x * 18) + 1, this.getAbsoluteY() + (y * 18) + 1);
                slot.setFilter(filter);
                for (ChangeListener listener : listeners) {
                    slot.addChangeListener(this, listener);
                }
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

    public void addChangeListener(ChangeListener listener) {
        Objects.requireNonNull(listener, "listener");
        listeners.add(listener);

        for (IndicatorSlot peer : peers) {
            peer.addChangeListener(this, listener);
        }
    }

    protected IndicatorSlot createSlotPeer(Inventory inventory, int index, int x, int y) {
        return new IndicatorSlot(inventory, index, x, y);
    }

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
    public void onHidden() {
        super.onHidden();

        for (IndicatorSlot peer : peers) {
            peer.setVisible(false);
        }
    }

    @FunctionalInterface
    public interface ChangeListener {
        void onStackChanged(WIndicatorItemSlot slot, Inventory inventory, int index, ItemStack stack);
    }
}

