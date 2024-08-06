package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.util.MathHelper;
import io.github.cottonmc.cotton.gui.client.Scissors;
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
import java.util.function.Consumer;

public class WLevelSlider extends WWidget {
    public static final Texture FADE_MASK_RIGHT = new Texture(new Identifier(Main.MODID, "textures/fade_mask_32x32.png"));
    public static final Texture FADE_MASK_LEFT = new Texture(new Identifier(Main.MODID, "textures/fade_mask_32x32.png"), 1.0f, 0.0f, 0.0f, 1.0f);
    // As defined in TextRenderer#fontHeight
    static final int FONT_HEIGHT = 9;
    protected static final double MASS = 1.0d;
    protected static final double F_PULL = 40.0d;
    protected static final double K_FRICTION = 8d;

    protected int minLevel = 1;
    protected int maxLevel = 10;
    protected int level = 1;
    protected float pos = 1.0f;
    protected int lastLevel = 1;
    protected double v = 0.0f; // v stands for velocity, v = dpos / dt, t has the unit of sec.
    protected float gap = 20;
    protected long lastPaintNano = 0;
    protected DragTracker dragTracker = new DragTracker(8);
    protected TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    protected Consumer<Integer> changedListener = null;


    public WLevelSlider() {
        super();
        for (int i = 0; i < 11; i++) {
            String roman = MathHelper.roman(i);
            System.out.println(roman + ": " + textRenderer.getWidth(roman));
        }
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        Scissors.push(x, y, getWidth(), getHeight());
        long nano = System.nanoTime();
        if (dragTracker.getSampleCount() == 0 && lastPaintNano != 0) {
            long delta = nano - lastPaintNano;
            pos += (float) (v * delta / 1E9);
            handleLevelChange();

            double distance = MathHelper.clamp(Math.round(pos), minLevel, maxLevel) - pos;double effectiveDistance = pos >= minLevel && pos <= maxLevel ? distance : distance * 2;
            double pull = effectiveDistance * F_PULL;
            double friction = - v * K_FRICTION;
            double a = (pull + friction) / MASS;
            v += a * delta / 1E9;
        }
        lastPaintNano = nano;

        float rightBound = pos * gap + width / 2.0f;
        float leftBound = pos * gap - width / 2.0f;
        int highestLevelToRender = (int) Math.min(maxLevel, Math.ceil(rightBound / gap));
        if (gap * highestLevelToRender - textRenderer.getWidth(MathHelper.roman(highestLevelToRender)) / 2.0f >= rightBound)
            highestLevelToRender--;
        int lowestLevelToRender = (int) Math.max(minLevel, Math.floor(leftBound / gap));
        if (gap * lowestLevelToRender + textRenderer.getWidth(MathHelper.roman(lowestLevelToRender)) / 2.0f <= leftBound)
            lowestLevelToRender++;
        for (int lvl = lowestLevelToRender; lvl <= highestLevelToRender; lvl++) {
            String roman = MathHelper.roman(lvl);
            int textWidth = textRenderer.getWidth(roman);
            textRenderer.drawWithShadow(matrices, roman,
                    x + width / 2.0f + (lvl - pos) * gap - textWidth / 2.0f,
                    y + height / 2.0f - FONT_HEIGHT / 2.0f,
                    0x00_FFFFFF
            );
        }

        ScreenDrawing.texturedRect(matrices, x, y, 16, height, FADE_MASK_LEFT, 0xFF_C6C6C6);
        ScreenDrawing.texturedRect(matrices, x + width - 16, y, 16, height, FADE_MASK_RIGHT, 0xFF_C6C6C6);
        Scissors.pop();
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
        // For example, when it just got clicked.
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
            dPos = -deltaX / gap;
        } else {
            double bound = pos > maxLevel ? maxLevel : minLevel;
            dPos = -deltaX / gap * (1 / Math.exp(Math.pow(6 * (pos - bound), 2)));
        }
        pos += (float) dPos;
        handleLevelChange();
        dragTracker.push(pos);
        return InputResult.PROCESSED;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    public int level() {
        return MathHelper.clamp(Math.round(pos), minLevel, maxLevel);
    }

    protected void handleLevelChange() {
        int level = level();
        if (level != lastLevel && changedListener != null) {
            changedListener.accept(level);
        }
        lastLevel = level;
    }

    public WLevelSlider setChangedListener(Consumer<Integer> listener) {
        changedListener = listener;
        return this;
    }
}