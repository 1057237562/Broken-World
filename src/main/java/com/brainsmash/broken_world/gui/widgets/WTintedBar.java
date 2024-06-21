package com.brainsmash.broken_world.gui.widgets;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class WTintedBar extends WBar {
    protected int tint = 0xFF_FFFFFF;
    protected Texture background;
    protected Texture foreground;

    public WTintedBar(@Nullable Texture bg, @Nullable Texture bar, int field, int maxField) {
        super(bg, bar, field, maxField);
        background = bg;
        foreground = bar;
    }

    public WTintedBar(@Nullable Texture bg, @Nullable Texture bar, int field, int maxField, Direction dir) {
        super(bg, bar, field, maxField, dir);
        background = bg;
        foreground = bar;
    }

    public WTintedBar(Identifier bg, Identifier bar, int field, int maxField) {
        super(bg, bar, field, maxField);
        background = this.bg;
        foreground = this.bar;
    }

    public WTintedBar(Identifier bg, Identifier bar, int field, int maxField, Direction dir) {
        super(bg, bar, field, maxField, dir);
        background = this.bg;
        foreground = this.bar;
    }

    public void setBG(Texture bg) {
        background = bg;
    }

    public void setBG(Identifier bg) {
        background = new Texture(bg);
    }

    public void setBar(Texture bar) {
        foreground = bar;
    }

    public void setBar(Identifier bar) {
        foreground = new Texture(bar);
    }

    public void setTint(int tint) {
        this.tint = tint;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(DrawContext content, int x, int y, int mouseX, int mouseY) {
        if (background != null) {
            ScreenDrawing.texturedRect(content, x, y, getWidth(), getHeight(), background, tint);
        } else {
            ScreenDrawing.coloredRect(content, x, y, getWidth(), getHeight(),
                    ScreenDrawing.colorAtOpacity(0x000000, 0.25f));
        }

        int maxVal = max >= 0 ? properties.get(max) : maxValue;
        float percent = properties.get(field) / (float) maxVal;
        if (percent < 0) percent = 0f;
        if (percent > 1) percent = 1f;

        int barMax = getWidth();
        if (direction == Direction.DOWN || direction == Direction.UP) barMax = getHeight();
        percent = ((int) (percent * barMax)) / (float) barMax; //Quantize to bar size

        int barSize = (int) (barMax * percent);
        if (barSize <= 0) return;

        switch (direction) { //anonymous blocks in this switch statement are to sandbox variables
            case UP -> {
                int left = x;
                int top = y + getHeight();
                top -= barSize;
                if (foreground != null) {
                    ScreenDrawing.texturedRect(content, left, top, getWidth(), barSize, foreground.image(),
                            foreground.u1(), MathHelper.lerp(percent, foreground.v2(), foreground.v1()),
                            foreground.u2(), foreground.v2(), tint);
                } else {
                    ScreenDrawing.coloredRect(content, left, top, getWidth(), barSize,
                            ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }

            case RIGHT -> {
                if (foreground != null) {
                    ScreenDrawing.texturedRect(content, x, y, barSize, getHeight(), foreground.image(), foreground.u1(),
                            foreground.v1(), MathHelper.lerp(percent, foreground.u1(), foreground.u2()),
                            foreground.v2(), tint);
                } else {
                    ScreenDrawing.coloredRect(content, x, y, barSize, getHeight(),
                            ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }

            case DOWN -> {
                if (foreground != null) {
                    ScreenDrawing.texturedRect(content, x, y, getWidth(), barSize, foreground.image(), foreground.u1(),
                            foreground.v1(), foreground.u2(),
                            MathHelper.lerp(percent, foreground.v1(), foreground.v2()), tint);
                } else {
                    ScreenDrawing.coloredRect(content, x, y, getWidth(), barSize,
                            ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }

            case LEFT -> {
                int left = x + getWidth();
                int top = y;
                left -= barSize;
                if (foreground != null) {
                    ScreenDrawing.texturedRect(content, left, top, barSize, getHeight(), foreground.image(),
                            MathHelper.lerp(percent, foreground.u2(), foreground.u1()), foreground.v1(),
                            foreground.u2(), foreground.v2(), tint);
                } else {
                    ScreenDrawing.coloredRect(content, left, top, barSize, getHeight(),
                            ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }
        }
    }
}
