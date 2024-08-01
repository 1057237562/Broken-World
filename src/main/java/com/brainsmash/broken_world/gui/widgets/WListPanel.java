package com.brainsmash.broken_world.gui.widgets;

import io.github.cottonmc.cotton.gui.widget.WWidget;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class WListPanel<D, W extends WWidget> extends io.github.cottonmc.cotton.gui.widget.WListPanel<D, W> {
    public WListPanel(List<D> data, Supplier<W> supplier, BiConsumer<D, W> configurator) {
        super(data, supplier, configurator);
    }

    public io.github.cottonmc.cotton.gui.widget.WListPanel<D, W> setMargin(int margin) {
        this.margin = margin;
        return this;
    }
}