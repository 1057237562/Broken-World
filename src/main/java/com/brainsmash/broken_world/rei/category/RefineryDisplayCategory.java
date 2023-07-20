package com.brainsmash.broken_world.rei.category;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.rei.REIClient;
import com.brainsmash.broken_world.rei.display.RefineryDisplay;
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

public class RefineryDisplayCategory implements DisplayCategory<RefineryDisplay> {
    @Override
    public CategoryIdentifier<? extends RefineryDisplay> getCategoryIdentifier() {
        return REIClient.REFINERY_DISPLAY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.broken_world.refinery");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BlockRegister.blockitems[BlockRegistry.REFINERY.ordinal()]);
    }

    @Override
    public List<Widget> setupDisplay(RefineryDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 50, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 45, startPoint.y + 4)));


        widgets.add(Widgets.createSlot(new Point(startPoint.x + 79, startPoint.y + 5)).entries(
                display.getOutputEntries().get(0)).markOutput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(
                display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 22, startPoint.y + 5)).entries(
                display.getInputEntries().get(1)).markInput());

        return widgets;
    }
}
