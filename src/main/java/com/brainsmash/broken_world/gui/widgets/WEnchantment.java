package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.util.MathHelper;
import com.brainsmash.broken_world.util.MiscHelper;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Widget for displaying enchantments, as in vanilla Enchanting Table, but designed specially for Infusing Table.
 */
public class WEnchantment extends WWidget {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/enchanting_table.png");
    public static final Identifier ORB_AND_NUMBERS = new Identifier(Main.MODID, "textures/gui/infusion_table/orb_and_numbers.png");
    public static final int WIDTH = 108;
    public static final int HEIGHT = 19;
    public static final Texture BACKGROUND_AVAILABLE = new Texture(TEXTURE, 0, 166 / 256.0f, WIDTH / 256.0f, (166 + HEIGHT) / 256.0f);
    public static final Texture BACKGROUND_UNAVAILABLE = new Texture(TEXTURE, 0, 185 / 256.0f, WIDTH / 256.0f, (185 + HEIGHT) / 256.0f);
    public static final Texture BACKGROUND_MOUSE_HOVER = new Texture(TEXTURE, 0, 204 / 256.0f, WIDTH / 256.0f, (204 + HEIGHT) / 256.0f);
    public static final Texture ORB_BRIGHT_SMALL = new Texture(ORB_AND_NUMBERS, 0, 0, 16 / 128.0f, 16 / 128.0f);
    public static final Texture ORB_BRIGHT_MEDIUM = new Texture(ORB_AND_NUMBERS, 16 / 128.0f, 0, 32 / 128.0f, 16 / 128.0f);
    public static final Texture ORB_BRIGHT_LARGE = new Texture(ORB_AND_NUMBERS, 32 / 128.0f, 0, 48 / 128.0f, 16 / 128.0f);
    public static final Texture ORB_DIM_SMALL = new Texture(ORB_AND_NUMBERS, 0, 16 / 128.0f, 16 / 128.0f, 32 / 128.0f);
    public static final Texture ORB_DIM_MEDIUM = new Texture(ORB_AND_NUMBERS, 16 / 128.0f, 16 / 128.0f, 32 / 128.0f, 32 / 128.0f);
    public static final Texture ORB_DIM_LARGE = new Texture(ORB_AND_NUMBERS, 32 / 128.0f, 16 / 128.0f, 48 / 128.0f, 32 / 128.0f);
    public static final Texture[] NUMBER_FOREGROUND = generateNumberTextures(true);
    public static final Texture[] NUMBER_BACKGROUND = generateNumberTextures(false);

    public Enchantment enchantment = null;
    public int power = 0;
    public int level = 1;
    public int seed;
    protected TextRenderer textRenderer;
    public boolean available = true;
    protected Consumer<WEnchantment> clickedListener = null;
    protected List<Text> extraTooltips = new ArrayList<>();

    public WEnchantment() {
        super();
        textRenderer = MinecraftClient.getInstance().textRenderer;
        width = WIDTH;
        height = HEIGHT;
    }
    
    protected static Texture[] generateNumberTextures(boolean fg) {
        Texture[] array = new Texture[10];
        for (int i = 0; i < 10; i++) {
            array[i] = new Texture(ORB_AND_NUMBERS, (i * 8) / 128.0f, (fg ? 32 : 41) / 128.0f, (i * 8 + 8) / 128.0f, (fg ? 41 : 50) / 128.0f);
        }
        return array;
    }

    protected void drawOrbAndLevelNumbers(MatrixStack matrices, int x, int y, int level, boolean available) {
        if (level == 0)
            return;
        Texture orb;
        if (level <= 10) {
            orb = available ? ORB_BRIGHT_SMALL : ORB_DIM_SMALL;
        } else if (level <= 20) {
            orb = available ? ORB_BRIGHT_MEDIUM : ORB_DIM_MEDIUM;
        } else {
            orb = available ? ORB_BRIGHT_LARGE : ORB_DIM_LARGE;
        }
        ScreenDrawing.texturedRect(matrices, x + 1, y + 1, 16, 16, orb, 0xFFFFFFFF);
        int[] digits = MathHelper.digits(level);
        int _x = 9;
        for (int i = digits.length - 1; i >= 0; i--) {
            ScreenDrawing.texturedRect(matrices, x + _x, y + 3, 8, 9, NUMBER_BACKGROUND[digits[i]], available ? 0x00_2d2102 : 0x00_47352f);
            ScreenDrawing.texturedRect(matrices, x + _x, y + 3, 8, 9, NUMBER_FOREGROUND[digits[i]], available ? 0x00_c8ff8f : 0x00_8c605d);
            _x += digits[i] != 1 ? 7 : 5;
        }
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
//        /*
//        RenderSystem.viewport(0, 0, this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
//        RenderSystem.restoreProjectionMatrix();
//        DiffuseLighting.enableGuiDepthLighting();
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantingPhrases.getInstance().setSeed(seed);
//        int n = ((EnchantmentScreenHandler)this.handler).getLapisCount();

//        int widgetX = i + 60;
        int phrasesX = x + 13 + 7 * MathHelper.digits(power).length;
        int phrasesY = y + 2;
//        this.setZOffset(0);
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//        int power = ((EnchantmentScreenHandler)this.handler).enchantmentPower[o];
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (enchantment == null) {
            ScreenDrawing.texturedRect(matrices, x, y, WIDTH, HEIGHT, BACKGROUND_UNAVAILABLE, 0xFFFFFFFF);
            return;
        }
        int phrasesWidthLimit = 93 - 7 * MathHelper.digits(power).length;
        StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, phrasesWidthLimit);
        int t = 6839882;
        if (!available) {
            ScreenDrawing.texturedRect(matrices, x, y, WIDTH, HEIGHT, BACKGROUND_UNAVAILABLE, 0xFFFFFFFF);
            // draw exp orbs
            drawOrbAndLevelNumbers(matrices, x, y, power, available);
//            this.drawTexture(matrices, widgetX + 1, j + 15 + 19 * o, 16 * o, 239, 16, 16);
            this.textRenderer.drawTrimmed(stringVisitable, phrasesX, phrasesY, phrasesWidthLimit, (t & 0xFEFEFE) >> 1);
//            ScreenDrawing.drawString(matrices, stringVisitable.getString(), phrasesX, 2, 0xFF_000000 + (t & 0xFEFEFE) >> 1);
            t = 4226832;
        } else {
            if (mouseX >= 0 && mouseY >= 0 && mouseX < WIDTH && mouseY < HEIGHT) {
                ScreenDrawing.texturedRect(matrices, x, y, WIDTH, HEIGHT, BACKGROUND_MOUSE_HOVER, 0xFFFFFFFF);
                t = 0xFFFF80;
            } else {
                ScreenDrawing.texturedRect(matrices, x, y, WIDTH, HEIGHT, BACKGROUND_AVAILABLE, 0xFFFFFFFF);
            }
            // draw exp orbs
            drawOrbAndLevelNumbers(matrices, x, y, power, available);
//            this.drawTexture(matrices, widgetX + 1, j + 15 + 19 * o, 16 * o, 223, 16, 16);
            this.textRenderer.drawTrimmed(stringVisitable, phrasesX, phrasesY, phrasesWidthLimit, t);
//            ScreenDrawing.drawString(matrices, stringVisitable.getString(), phrasesX, 2, 0xFF_000000 + t);
            t = 8453920;
        }
//        this.textRenderer.drawWithShadow(matrices, powerString, (float)(phrasesX + 86 - this.textRenderer.getWidth(powerString)), 9.0f, t);
//         */
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if (enchantment == null) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        tooltip.add(Text.translatable("container.enchant.clue", power > 0 ? enchantment.getName(level) : MiscHelper.getEnchantmentName(enchantment)).formatted(Formatting.WHITE));
        // Vanilla Enchanting Table doesn't check null value, so adding assert here to shut IDEA up.
        assert client.player != null;
        if (!client.player.getAbilities().creativeMode) {
            if (power > 0) {
                tooltip.add(ScreenTexts.EMPTY);
                if (client.player.experienceLevel < power) {
                    tooltip.add(Text.translatable("container.enchant.level.requirement", power).formatted(Formatting.RED));
                } else {
//                MutableText mutableText = m == 1 ? Text.translatable("container.enchant.lapis.one") : Text.translatable("container.enchant.lapis.many", m);
//                tooltip.add(mutableText.formatted(i >= m ? Formatting.GRAY : Formatting.RED));
                    MutableText mutableText2 = power == 1 ? Text.translatable("container.enchant.level.one") : Text.translatable("container.enchant.level.many", power);
                    tooltip.add(mutableText2.formatted(Formatting.GRAY));
                }
            }
        }

        for (Text text : extraTooltips) {
            tooltip.add(text);
        }
    }

    public void setClickedListener(Consumer<WEnchantment> listener) {
        clickedListener = listener;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (clickedListener != null && enchantment != null && available) {
            clickedListener.accept(this);
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }

    /**
     * This widget is not resizable.
     * @param x ignored
     * @param y ignored
     */
    @Override
    public void setSize(int x, int y) {
    }

    public void addExtraTooltip(Text text) {
        extraTooltips.add(text);
    }
}
