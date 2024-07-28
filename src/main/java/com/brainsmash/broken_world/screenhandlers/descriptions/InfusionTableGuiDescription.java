package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.gui.widgets.WEnchantment;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.search.TextSearchProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class InfusionTableGuiDescription extends SyncedGuiDescription {
    public static int INVENTORY_SIZE = 1;
    public static int PROPERTY_COUNT = 0;
    public static final SearchManager.Key<Enchantment> ENCHANTMENT_KEY = new SearchManager.Key<>();

    protected SearchManager searchManager = new SearchManager();
    protected List<Enchantment> enchantments = new ArrayList<>();

    public InfusionTableGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.INFUSION_TABLE_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, PROPERTY_COUNT));
        searchManager.put(ENCHANTMENT_KEY, enchantments -> new TextSearchProvider<>(
                enchantment -> Stream.of(Text.translatable(enchantment.getTranslationKey()).toString()),
                enchantment -> Stream.of(Registry.ENCHANTMENT.getId(enchantment)),
                enchantments
        ));

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(150, 193);

        WTextField searchField = new WTextField();
        WListPanel<Enchantment, WEnchantment> enchantmentList = new WListPanel<>(enchantments, WEnchantment::new, (enchantment, widget) -> {
            widget.enchantmentID = Registry.ENCHANTMENT.getRawId(enchantment);
            widget.enchantmentPower = 0;
            widget.available = true;
            widget.seed = 0; // TODO add seed
        });
        searchField.setChangedListener(string -> {
            enchantments.clear();
            enchantments.addAll(findEnchantments(getSuitableEnchantments(blockInventory.getStack(0)), string));
        });
        enchantmentList.setListItemHeight(WEnchantment.HEIGHT * 3);
        root.add(searchField, 0, 4);
        root.add(enchantmentList, 0, 20);

        WItemSlot slot = WItemSlot.of(blockInventory, 0);
        root.add(slot, 140, 20);
        root.add(createPlayerInventoryPanel(), 0, 80);

        root.validate(this);
    }

    public static List<Enchantment> getSuitableEnchantments(ItemStack stack) {
        if (stack.isEmpty())
            return new ArrayList<>();
        boolean isBook = stack.isOf(Items.BOOK);
        Item item = stack.getItem();
        ArrayList<Enchantment> list = new ArrayList<>();
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            // Vanilla Enchanting Table disallows enchantments that are not available for random selection.
            // Read EnchantmentHelper#getPossibleEntries.
            if (enchantment.isTreasure() || !enchantment.isAvailableForRandomSelection() || !enchantment.type.isAcceptableItem(item) && !isBook) continue;
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                list.add(enchantment);
            }
        }
        return list;
    }

    public List<Enchantment> findEnchantments(List<Enchantment> list, String string) {
        return searchManager.get(ENCHANTMENT_KEY).findAll(string.toLowerCase(Locale.ROOT));
    }
}
