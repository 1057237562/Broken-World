package com.brainsmash.broken_world.rei.category;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.rei.REIClient;
import com.brainsmash.broken_world.rei.display.SifterDisplay;
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

public class SifterDisplayCategory implements DisplayCategory<SifterDisplay> {
    @Override
    public CategoryIdentifier<? extends SifterDisplay> getCategoryIdentifier() {
        return REIClient.SIFTER_DISPLAY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.broken_world.sifter");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BlockRegister.blockitems[BlockRegistry.SIFTER.ordinal()]);
    }

    @Override
    public List<Widget> setupDisplay(SifterDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 32 - Math.min(4, display.olist.size()) * 9,
                bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(
                display.getInputEntries().get(0)).markOutput());

        for (int i = 0; i < display.olist.size(); i++) {
            widgets.add(Widgets.withTooltip(Widgets.createSlot(new Point(startPoint.x + 61 + 18 * (i % 4),
                            startPoint.y + 5 - (display.olist.size() / 4) * 9 + (i / 4) * 18)).entries(
                            display.getOutputEntries().get(i)).markInput(),
                    Text.of(display.olist.get(i).getLeft() * 100.0 + "%")));
        }


        return widgets;
    }
}
