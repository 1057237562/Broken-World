package com.brainsmash.broken_world.rei;

import com.brainsmash.broken_world.recipe.AssemblerRecipe;
import com.brainsmash.broken_world.recipe.CompressorRecipe;
import com.brainsmash.broken_world.recipe.ExtractorRecipe;
import com.brainsmash.broken_world.recipe.RefineryRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.rei.category.AssemblerDisplayCategory;
import com.brainsmash.broken_world.rei.category.CompressorDisplayCategory;
import com.brainsmash.broken_world.rei.category.ExtractorDisplayCategory;
import com.brainsmash.broken_world.rei.category.RefineryDisplayCategory;
import com.brainsmash.broken_world.rei.display.AssemblerDisplay;
import com.brainsmash.broken_world.rei.display.CompressorDisplay;
import com.brainsmash.broken_world.rei.display.ExtractorDisplay;
import com.brainsmash.broken_world.rei.display.RefineryDisplay;
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
    }
}