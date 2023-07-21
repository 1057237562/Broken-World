package com.brainsmash.broken_world.rei;

import com.brainsmash.broken_world.recipe.*;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.rei.category.*;
import com.brainsmash.broken_world.rei.display.*;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

import static com.brainsmash.broken_world.Main.MODID;

public class REIClient implements REIClientPlugin {
    public static final CategoryIdentifier<ExtractorDisplay> EXTRACTOR_DISPLAY = CategoryIdentifier.of(MODID,
            "extractor");

    public static final CategoryIdentifier<CompressorDisplay> COMPRESSOR_DISPLAY = CategoryIdentifier.of(MODID,
            "compressor");

    public static final CategoryIdentifier<AssemblerDisplay> ASSEMBLER_DISPLAY = CategoryIdentifier.of(MODID,
            "assembler");

    public static final CategoryIdentifier<RefineryDisplay> REFINERY_DISPLAY = CategoryIdentifier.of(MODID, "refinery");

    public static final CategoryIdentifier<CrusherDisplay> CRUSHER_DISPLAY = CategoryIdentifier.of(MODID, "crusher");
    public static final CategoryIdentifier<SifterDisplay> SIFTER_DISPLAY = CategoryIdentifier.of(MODID, "sifter");
    public static final CategoryIdentifier<AdvancedFurnaceDisplay> ADVANCED_FURNACE_DISPLAY = CategoryIdentifier.of(
            MODID, "advanced_furnace");

    public static final CategoryIdentifier<ReactionDisplay> REACTION_DISPLAY = CategoryIdentifier.of(MODID, "reaction");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ExtractorDisplayCategory());
        registry.addWorkstations(EXTRACTOR_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.EXTRACTOR.ordinal()]));

        registry.add(new CompressorDisplayCategory());
        registry.addWorkstations(COMPRESSOR_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.COMPRESSOR.ordinal()]));

        registry.add(new AssemblerDisplayCategory());
        registry.addWorkstations(ASSEMBLER_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.ASSEMBLER.ordinal()]));

        registry.add(new RefineryDisplayCategory());
        registry.addWorkstations(REFINERY_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.REFINERY.ordinal()]));

        registry.add(new CrusherDisplayCategory());
        registry.addWorkstations(CRUSHER_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.CRUSHER.ordinal()]));

        registry.add(new SifterDisplayCategory());
        registry.addWorkstations(SIFTER_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.SIFTER.ordinal()]));

        registry.add(new AdvancedFurnaceDisplayCategory());
        registry.addWorkstations(ADVANCED_FURNACE_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.ADVANCED_FURNACE.ordinal()]));

        registry.add(new ReactionDisplayCategory());
        registry.addWorkstations(REACTION_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.REACTION_KETTLE.ordinal()]));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ExtractorRecipe.recipes.forEach((item, pairs) -> registry.add(new ExtractorDisplay(item, pairs)));
        CompressorRecipe.recipes.forEach((item, stack) -> registry.add(
                new CompressorDisplay(new ItemStack(item, CompressorRecipe.counts.get(item)), stack)));
        AssemblerRecipe.rei.forEach((item, output) -> registry.add(
                new AssemblerDisplay(Arrays.asList(item.getLeft().getDefaultStack(), item.getRight().getDefaultStack()),
                        output)));
        RefineryRecipe.rei.forEach((item, stack) -> registry.add(
                new RefineryDisplay(Arrays.asList(item.getLeft(), item.getRight()), stack)));
        CrusherRecipe.recipes.forEach((item, stack) -> registry.add(new CrusherDisplay(item, stack)));
        SifterRecipe.recipes.forEach((item, stack) -> registry.add(new SifterDisplay(item, stack)));
        AdvancedFurnaceRecipe.recipes.forEach((item, stack) -> registry.add(new AdvancedFurnaceDisplay(item, stack)));
        ReactionRecipe.rei.forEach((item, stack) -> registry.add(new ReactionDisplay(item, stack)));
    }
}
