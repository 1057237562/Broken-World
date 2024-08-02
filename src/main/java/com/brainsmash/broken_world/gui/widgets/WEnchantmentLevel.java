package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.util.MathHelper;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayDeque;
import java.util.Deque;

public class WEnchantmentLevel extends WWidget {
    public static final Texture MASK = new Texture(new Identifier(Main.MODID, "textures/gui/infusion_table/mask.png"));
    // As defined in TextRenderer#fontHeight
    static final int FONT_HEIGHT = 9;
    protected static final double MASS = 1.0d;
    protected static final double F_PULL = 2.0d;
    protected static final double K_FRICTION = 4d;

    protected int minLevel = 1;
    protected int maxLevel = 1;
    protected int level = 1;
    protected float pos = 1.0f;
    protected double v = 0.0f; // v stands for velocity, v = dpos / dt, t has the unit of sec.
    protected float gap = 16;
    protected long lastPaintNano = 0;
    protected DragTracker dragTracker = new DragTracker(8);
    protected TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public WEnchantmentLevel() {
        super();
    }

    public WEnchantmentLevel setMinLevel(int minLevel) {
        this.minLevel = minLevel;
        return this;
    }

    public WEnchantmentLevel setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        long nano = System.nanoTime();
        if (dragTracker.getSampleCount() == 0 && lastPaintNano != 0) {
            long delta = nano - lastPaintNano;
            pos += (float) (v * delta / 1E9);
//            v += (pos - net.minecraft.util.math.MathHelper.clamp(Math.round(pos), minLevel, maxLevel) * F_PULL - v * K_FRICTION) / MASS;
            double distance = MathHelper.clamp(Math.round(pos), minLevel, maxLevel) - pos;
//            double effectiveDistance = MathHelper.clamp(distance, -0.5, 0.5);
            double effectiveDistance = pos >= minLevel && pos <= maxLevel ? distance : distance * 2;
            double pull = effectiveDistance * 40.0d; // TODO Replace this constant with F_PULL
            double friction = - v * 8d; // TODO Replace this constant with K_FRICTION
//            double friction = (v >= 0 ? -1 : 1) * (Math.exp(Math.abs(v)) - 1) * 4d;
            double a = (pull + friction) / MASS;
            v += a * delta / 1E9;
        }
        lastPaintNano = nano;

        float upperBound = pos * gap + height / 2.0f;
        float lowerBound = pos * gap - height / 2.0f;
        int highestLevelToRender = (int) Math.min(maxLevel, Math.floor((upperBound + FONT_HEIGHT / 2.0f) / gap));
        int lowestLevelToRender = (int) Math.max(minLevel, Math.ceil((lowerBound - FONT_HEIGHT / 2.0f) / gap));
        for (int lvl = lowestLevelToRender; lvl <= highestLevelToRender; lvl++) {
            String roman = MathHelper.roman(lvl);
            int width = textRenderer.getWidth(roman);
            textRenderer.drawWithShadow(matrices, roman, x + this.width / 2.0f - width / 2.0f,
                    y + (upperBound - (lvl * gap + FONT_HEIGHT / 2.0f)), 0x00_FFFFFF);
        }
        ScreenDrawing.texturedRect(matrices, x, y, width, height, MASK, 0xFF_FFFFFF);
    }

    protected static class DragTracker {
        int maxSampleCount;

        private record Sample(double pos, long nano) {
        }

        Deque<Sample> samples;

        /**
         * @param sampleCount How many samples to keep hold of.
         */
        public DragTracker(int sampleCount) {
            if (sampleCount <= 0) {
                throw new IllegalArgumentException("sampleCount must be positive, given value is " + sampleCount);
            }
            maxSampleCount = sampleCount;
            samples = new ArrayDeque<>();
        }

        /**
         * Retrieves average speed of latest recorded samples. Unit of time is in second.
         *
         * @return 0 if recorded samples count is less than max samples count; Average speed otherwise.
         */
        public double speed() {
            if (samples.size() < maxSampleCount || samples.size() < 2) {
                return 0;
            }

            return (samples.peekLast().pos - samples.peekFirst().pos) / (samples.peekLast().nano - samples.peekFirst().nano) * 1E9;
        }

        public void push(double pos) {
            push(pos, System.nanoTime());
        }

        public void push(double pos, long nano) {
            if (samples.size() == maxSampleCount) {
                samples.remove();
            }
            samples.add(new Sample(pos, nano));
        }

        public void clear() {
            samples.clear();
        }

        public int getSampleCount() {
            return samples.size();
        }
    }

    @Override
    public InputResult onMouseDown(int x, int y, int button) {
        if (button != 0) {
            return InputResult.IGNORED;
        }
        dragTracker.push(pos);
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onMouseUp(int x, int y, int button) {
        if (button != 0) { // 0 represents left button.
            return InputResult.IGNORED;
        }
        dragTracker.push(pos);

        // If there's only 1 or 2 samples, perhaps the widget is not dragged at all.
        // For example it might just got clicked.
        if (dragTracker.getSampleCount() >= 3) {
            v = dragTracker.speed();
        }
        dragTracker.clear();
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        if (button != 0) {
            return InputResult.IGNORED;
        }
        double dPos;
        if (pos >= minLevel && pos <= maxLevel) {
            dPos = deltaY / gap;
        } else {
            double bound = pos > maxLevel ? maxLevel : minLevel;
            dPos = deltaY / gap * (1 / Math.exp(Math.pow(6 * (pos - bound), 2)));
        }
        pos += (float) dPos;
        dragTracker.push(pos);
        return InputResult.PROCESSED;
    }
}