package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.gui.widgets.WEnchantment;
import com.brainsmash.broken_world.gui.widgets.WEnchantmentLevel;
import com.brainsmash.broken_world.gui.widgets.WListPanel;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.search.TextSearchProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class InfusionTableGuiDescription extends SyncedGuiDescription {
    public static int INVENTORY_SIZE = 1;
    public static int PROPERTY_COUNT = 1;
    public static final SearchManager.Key<Enchantment> ENCHANTMENT_KEY = new SearchManager.Key<>();

    protected SearchManager searchManager = new SearchManager();
    protected List<Enchantment> enchantments = new ArrayList<>();
    protected ScreenHandlerContext context;
    protected WTextField searchField;
    protected WListPanel<Enchantment, WEnchantment> enchantmentList;
    protected Inventory inventory = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            onContentChanged(this);
        }
    };
    protected Property seed = Property.create();

    public InfusionTableGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.INFUSION_TABLE_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));

        this.context = context;
        searchManager.put(ENCHANTMENT_KEY, enchantments -> new TextSearchProvider<>(enchantment -> Stream.of(
                Formatting.strip(Text.translatable(enchantment.getTranslationKey()).getString())),
                enchantment -> Stream.of(Registry.ENCHANTMENT.getId(enchantment)), enchantments));
        addProperty(seed).set(playerInventory.player.getEnchantmentTableSeed());

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(150, 193);

        searchField = new WTextField();
        enchantmentList = new WListPanel<>(enchantments, WEnchantment::new, (enchantment, widget) -> {
            widget.enchantment = enchantment;
            widget.enchantmentPower = 0;
            widget.available = true;
            widget.seed = seed.get() & enchantment.hashCode();
        });
        searchField.setChangedListener(string -> refreshEnchantmentList()).setSuggestion(
                Text.translatable("gui.infusion_table.search_hint"));
        enchantmentList.setMargin(0).setListItemHeight(WEnchantment.HEIGHT);
//        root.add(searchField, 0, 6, WEnchantment.WIDTH + enchantmentList.getScrollBar().getWidth(), searchField.getHeight());
        // 8 is the width of WListPanel's scrollbar, which is constant and hardcoded.
        root.add(searchField, 0, 6, WEnchantment.WIDTH + 8, searchField.getHeight());
        root.add(enchantmentList, 0, 6 + searchField.getHeight(), WEnchantment.WIDTH + 8, WEnchantment.HEIGHT * 3);

        WEnchantmentLevel levelSlider = new WEnchantmentLevel();
        levelSlider.setMaxLevel(50);
        WItemSlot slot = WItemSlot.of(inventory, 0);
        root.add(levelSlider, 120, 20, 40, 80);
        root.add(slot, 140, 40);
        root.add(createPlayerInventoryPanel(), 0, 80);

        root.validate(this);
    }

    @Override
    public void close(PlayerEntity player) {
        context.run((world, pos) -> this.dropInventory(player, inventory));
    }

    public static List<Enchantment> getSuitableEnchantments(ItemStack stack) {
        if (stack.isEmpty()) return new ArrayList<>();
        boolean isBook = stack.isOf(Items.BOOK);
        Item item = stack.getItem();
        ArrayList<Enchantment> list = new ArrayList<>();
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            // Vanilla Enchanting Table disallows enchantments that are not available for random selection.
            // Read EnchantmentHelper#getPossibleEntries.
            if (enchantment.isTreasure() || !enchantment.isAvailableForRandomSelection() || !enchantment.type.isAcceptableItem(
                    item) && !isBook) continue;

            list.add(enchantment);
        }
        return list;
    }

    public List<Enchantment> findEnchantments(List<Enchantment> list, String string) {
        searchManager.reload(ENCHANTMENT_KEY, list);
        return searchManager.get(ENCHANTMENT_KEY).findAll(string.toLowerCase(Locale.ROOT));
    }

    protected void refreshEnchantmentList() {
        enchantments.clear();
        enchantments.addAll(findEnchantments(getSuitableEnchantments(inventory.getStack(0)), searchField.getText()));
        enchantmentList.layout();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (inventory != this.inventory) {
            return;
        }

        searchField.setText("");
        refreshEnchantmentList();
    }
}