package com.brainsmash.broken_world.rei.display;

import com.brainsmash.broken_world.rei.REIClient;
import com.brainsmash.broken_world.util.ItemHelper;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.List;

public class CrusherDisplay extends BasicDisplay {

    public List<Pair<Float, Item>> olist;

    public CrusherDisplay(Item input, List<Pair<Float, Item>> output) {
        this(Collections.singletonList(EntryIngredients.of(input)),
                EntryIngredients.ofIngredients(ItemHelper.fromItemStacks(ItemHelper.getItemstackFromPairs(output))));
        olist = output;
    }

    public CrusherDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIClient.CRUSHER_DISPLAY;
    }


}
