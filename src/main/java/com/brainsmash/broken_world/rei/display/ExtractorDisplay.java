package com.brainsmash.broken_world.rei.display;

import com.brainsmash.broken_world.rei.REIClient;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.List;

public class ExtractorDisplay extends BasicDisplay {

    public ExtractorDisplay(Item input, List<Pair<Float, Item>> output) {
        this(Collections.singletonList(EntryIngredients.of(input)),
                Collections.singletonList(EntryIngredients.of(output.get(0).getRight())));
    }

    public ExtractorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIClient.EXTRACTOR_DISPLAY;
    }


}
