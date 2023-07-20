package com.brainsmash.broken_world.rei;

import com.brainsmash.broken_world.recipe.ExtractorRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.rei.category.ExtractorDisplayCategory;
import com.brainsmash.broken_world.rei.display.ExtractorDisplay;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;

import static com.brainsmash.broken_world.Main.MODID;

public class REIClient implements REIClientPlugin {
    public static final CategoryIdentifier<ExtractorDisplay> EXTRACTOR_DISPLAY = CategoryIdentifier.of(MODID,
            "extractor");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ExtractorDisplayCategory());
        registry.addWorkstations(EXTRACTOR_DISPLAY,
                EntryStacks.of(BlockRegister.blockitems[BlockRegistry.EXTRACTOR.ordinal()]));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ExtractorRecipe.recipes.forEach((item, pairs) -> {
            registry.add(new ExtractorDisplay(item, pairs));
        });
    }
}
