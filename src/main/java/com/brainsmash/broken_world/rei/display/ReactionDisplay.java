package com.brainsmash.broken_world.rei.display;

import com.brainsmash.broken_world.rei.REIClient;
import com.brainsmash.broken_world.util.ItemHelper;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class ReactionDisplay extends BasicDisplay {

    public ReactionDisplay(List<ItemStack> input, ItemStack output) {
        this(EntryIngredients.ofIngredients(ItemHelper.fromItemStacks(input)),
                Collections.singletonList(EntryIngredients.of(output)));
    }

    public ReactionDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIClient.REACTION_DISPLAY;
    }


}
