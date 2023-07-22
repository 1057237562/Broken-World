package com.brainsmash.broken_world.rei.category;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.rei.REIClient;
import com.brainsmash.broken_world.rei.display.ReactionDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class ReactionDisplayCategory implements DisplayCategory<ReactionDisplay> {
    @Override
    public CategoryIdentifier<? extends ReactionDisplay> getCategoryIdentifier() {
        return REIClient.REACTION_DISPLAY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.broken_world.reaction");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BlockRegister.blockitems[BlockRegistry.REACTION_KETTLE.ordinal()]);
    }

    @Override
    public List<Widget> setupDisplay(ReactionDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 59, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 63, startPoint.y + 4)));


        widgets.add(Widgets.createSlot(new Point(startPoint.x + 97, startPoint.y + 5)).entries(
                display.getOutputEntries().get(0)).markOutput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(
                display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 22, startPoint.y + 5)).entries(
                display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 40, startPoint.y + 5)).entries(
                display.getInputEntries().get(2)).markInput());

        return widgets;
    }
}
