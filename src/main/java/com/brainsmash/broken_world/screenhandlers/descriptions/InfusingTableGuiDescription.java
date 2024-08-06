package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.magical.InfusingTableEntity;
import com.brainsmash.broken_world.gui.widgets.WEnchantment;
import com.brainsmash.broken_world.gui.widgets.WLevelSlider;
import com.brainsmash.broken_world.gui.widgets.WListPanel;
import com.brainsmash.broken_world.util.MiscHelper;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.search.TextSearchProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class InfusingTableGuiDescription extends SyncedGuiDescription {
    public static int INVENTORY_SIZE = 1;
    // 8 is the width of WListPanel's scrollbar, which is constant and hardcoded.
    protected static int SCROLLBAR_WIDTH = 8;
    public static final SearchManager.Key<Enchantment> ENCHANTMENT_KEY = new SearchManager.Key<>();
    public static final Icon SLOT_ICON = new TextureIcon(new Identifier(Main.MODID, "textures/gui/infusion_table/slot_icon.png"));

    protected SearchManager searchManager = new SearchManager();
    protected List<EnchantmentLevelEntry> enchantments = new ArrayList<>();
    protected ScreenHandlerContext context;
    protected WTextField searchField;
    protected WListPanel<EnchantmentLevelEntry, WEnchantment> enchantmentList;
    protected WItemSlot slot;
    protected WLevelSlider slider;
    protected Inventory inventory = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            onContentChanged(this);
        }
    };
    protected Property seed = Property.create();
    protected int syncId;

    public InfusingTableGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.INFUSION_TABLE_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE),
                getBlockPropertyDelegate(context, InfusingTableEntity.PROPERTY_COUNT));

        this.context = context;
        searchManager.put(ENCHANTMENT_KEY, enchantments -> new TextSearchProvider<>(enchantment -> Stream.of(
                Formatting.strip(Text.translatable(enchantment.getTranslationKey()).getString())),
                enchantment -> Stream.of(Registry.ENCHANTMENT.getId(enchantment)), enchantments));
        addProperty(seed).set(playerInventory.player.getEnchantmentTableSeed());
        this.syncId = syncId;

        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL).setSize(176, 166);
        setRootPanel(root);

        slider = new WLevelSlider();
        slider.setChangedListener(level -> {
            refreshEnchantmentList();
        });

        searchField = new WTextField();
        searchField.setChangedListener(string -> refreshEnchantmentList())
                .setSuggestion(Text.translatable("gui.infusion_table.search_hint"));

        slot = WItemSlot.of(inventory, 0);
        slot.setIcon(SLOT_ICON).addChangeListener((w, inv, stack, i) -> onContentChanged(inv));

        enchantmentList = new WListPanel<>(enchantments, WEnchantment::new, (entry, widget) -> {
            int level = entry.level;
            Enchantment e = widget.enchantment = entry.enchantment;
            PlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;
            int minLevel = e.getMinLevel(), maxLevel = e.getMaxLevel();
            if (minLevel <= level && level <= maxLevel) {
                widget.power = Math.round((e.getMaxPower(level) + e.getMinPower(level)) / 2.0f);
                widget.level = level;
                assert MinecraftClient.getInstance().player != null;
                widget.available = player.experienceLevel >= widget.power || player.getAbilities().creativeMode;
            } else {
                widget.power = 0;
                widget.level = 0;
                widget.available = false;
                if (minLevel > level) {
                    widget.addExtraTooltip(Text.translatable("container.enchant.level.min", minLevel).formatted(Formatting.RED));
                } else {
                    widget.addExtraTooltip(Text.translatable("container.enchant.level.max", maxLevel).formatted(Formatting.RED));
                }
            }
            widget.seed = seed.get() ^ e.hashCode() + level;
            widget.setClickedListener(w -> {
                assert MinecraftClient.getInstance().interactionManager != null;
                int id = Registry.ENCHANTMENT.getRawId(w.enchantment);
                if ((id & 0xFFFF0000) != 0) {
                    throw new RuntimeException(w.enchantment + " has id that is too large: " + id);
                }
                MinecraftClient.getInstance().interactionManager.clickButton(syncId, (id << 16) | w.level);
            });
        });
        enchantmentList.setMargin(0).setListItemHeight(WEnchantment.HEIGHT);

        root.add(searchField, 0, 7, WEnchantment.WIDTH + SCROLLBAR_WIDTH, searchField.getHeight());
        root.add(enchantmentList, 0, 7 + searchField.getHeight(), WEnchantment.WIDTH + 8, WEnchantment.HEIGHT * 3);
        root.add(slider,
                WEnchantment.WIDTH + SCROLLBAR_WIDTH, 24,
                root.getWidth() - 14 - WEnchantment.WIDTH - SCROLLBAR_WIDTH, 29
        );
        root.add(slot, 132, 65);

        root.add(createPlayerInventoryPanel(), 0, 90);

        root.validate(this);
    }

    @Override
    public void close(PlayerEntity player) {
        context.run((world, pos) -> this.dropInventory(player, inventory));
    }

    public static List<Enchantment> getSuitableEnchantments(ItemStack stack) {
//        if (stack.isEmpty()) return new ArrayList<>();
        boolean isBook = stack.isOf(Items.BOOK);
        Item item = stack.getItem();
        ArrayList<Enchantment> list = new ArrayList<>();
        if (!stack.isEmpty() && (!stack.isEnchantable() || item.getEnchantability() <= 0))
            return list;
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            // Vanilla Enchanting Table disallows enchantments that are not available for random selection.
            // Read EnchantmentHelper#getPossibleEntries.
            if (enchantment.isTreasure() ||
                    !enchantment.isAvailableForRandomSelection() ||
                    item != Items.AIR && !isBook && !enchantment.type.isAcceptableItem(item)
            ) continue;

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
        enchantments.addAll(findEnchantments(getSuitableEnchantments(inventory.getStack(0)), searchField.getText())
                .stream().map(e -> new EnchantmentLevelEntry(e, slider.level())).toList()
        );
        enchantmentList.layout();
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int buttonId) {
        int id = buttonId >> 16;
        int level = buttonId & 0xFFFF;

        ItemStack stack = inventory.getStack(0);
        if (stack.isEmpty())
            return false;

        Enchantment e = Registry.ENCHANTMENT.get(id);
        if (e == null) {
            Util.error(player.getName() + " wants to enchant with nonexistent enchantment with id " + id);
            Util.error("This shouldn't happen, neither there's a bug or the player is hacking?");
            return false;
        }

        if (!getSuitableEnchantments(stack).contains(e)) {
            Util.error(player.getName() + " wants to enchant " + stack + " with " + MiscHelper.getEnchantmentName(e));
            Util.error("This shouldn't happen, neither there's a bug or the player is hacking?");
            return false;
        }

        if (e.getMinLevel() > level || e.getMaxLevel() < level) {
            Util.error(player.getName() + " wants to enchant " + stack + " with " + e.getName(level).getString() +
                    " which falls outside of the enchantment's level range: [" + e.getMinLevel() + ", " + e.getMaxLevel() + "]");
            Util.error("This shouldn't happen, neither there's a bug or the player is hacking?");
            return false;
        }
        int power = Math.round((e.getMaxPower(level) + e.getMinPower(level)) / 2.0f);
        if (!player.getAbilities().creativeMode && player.experienceLevel < power) {
            Util.error(player.getName() + " wants to enchant " + stack + " with " + e.getName(level) +
                    ", but has insufficient exp level: " + player.experienceLevel + " < " + level);
            Util.error("This shouldn't happen, neither there's a bug or the player is hacking?");
            return false;
        }

        context.run((world, pos) -> {
            player.applyEnchantmentCosts(stack, power);
            boolean isBook = stack.isOf(Items.BOOK);
            if (isBook) {
                ItemStack stack2 = new ItemStack(Items.ENCHANTED_BOOK);
                NbtCompound nbtCompound = stack.getNbt();
                if (nbtCompound != null) {
                    stack2.setNbt(nbtCompound.copy());
                }
                this.inventory.setStack(0, stack2);
                EnchantedBookItem.addEnchantment(stack2, new EnchantmentLevelEntry(e, level));
            } else {
                stack.addEnchantment(e, level);
            }
            player.incrementStat(Stats.ENCHANT_ITEM);
            if (player instanceof ServerPlayerEntity) {
                Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, inventory.getStack(0), power);
            }
            this.inventory.markDirty();
            this.seed.set(player.getEnchantmentTableSeed());
            this.onContentChanged(this.inventory);
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.random.nextFloat() * 0.1f + 0.9f);
        });
        return true;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (inventory != this.inventory) {
            return;
        }

        searchField.setText("");
        slot.setIcon(inventory.getStack(0).isEmpty() ? SLOT_ICON : null);
        refreshEnchantmentList();
    }
}