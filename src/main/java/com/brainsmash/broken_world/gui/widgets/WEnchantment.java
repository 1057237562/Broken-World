package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.util.MathHelper;
import com.brainsmash.broken_world.util.MiscHelper;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class WEnchantment extends WWidget {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/enchanting_table.png");
    public static final Identifier ORB_AND_NUMBERS = new Identifier(Main.MODID,
            "textures/gui/infusion_table/orb_and_numbers.png");
    public static final int WIDTH = 108;
    public static final int HEIGHT = 19;
    public static final Texture BACKGROUND_AVAILABLE = new Texture(TEXTURE, 0, 166 / 256.0f, WIDTH / 256.0f,
            (166 + HEIGHT) / 256.0f);
    public static final Texture BACKGROUND_UNAVAILABLE = new Texture(TEXTURE, 0, 185 / 256.0f, WIDTH / 256.0f,
            (185 + HEIGHT) / 256.0f);
    public static final Texture BACKGROUND_MOUSE_HOVER = new Texture(TEXTURE, 0, 204 / 256.0f, WIDTH / 256.0f,
            (204 + HEIGHT) / 256.0f);
    public static final Texture ORB_BRIGHT_SMALL = new Texture(ORB_AND_NUMBERS, 0, 0, 16 / 128.0f, 16 / 128.0f);
    public static final Texture ORB_BRIGHT_MEDIUM = new Texture(ORB_AND_NUMBERS, 16 / 128.0f, 0, 32 / 128.0f,
            16 / 128.0f);
    public static final Texture ORB_BRIGHT_LARGE = new Texture(ORB_AND_NUMBERS, 32 / 128.0f, 0, 48 / 128.0f,
            16 / 128.0f);
    public static final Texture ORB_DIM_SMALL = new Texture(ORB_AND_NUMBERS, 0, 0, 16 / 128.0f, 16 / 128.0f);
    public static final Texture ORB_DIM_MEDIUM = new Texture(ORB_AND_NUMBERS, 0, 0, 16 / 128.0f, 16 / 128.0f);
    public static final Texture ORB_DIM_LARGE = new Texture(ORB_AND_NUMBERS, 0, 0, 16 / 128.0f, 16 / 128.0f);


    public Enchantment enchantment = null;
    public int enchantmentPower = 0;
    public int seed;
    protected TextRenderer textRenderer;
    public boolean available = true;
    protected Texture[] numberForegrounds;
    protected Texture[] numberBackgrounds;

    public WEnchantment() {
        super();
        textRenderer = MinecraftClient.getInstance().textRenderer;
        numberForegrounds = new Texture[10];
        numberBackgrounds = new Texture[10];
        for (int i = 0; i < 10; i++) {
            numberForegrounds[i] = new Texture(ORB_AND_NUMBERS, (i * 8) / 128.0f, 32 / 128.0f, (i * 8 + 8) / 128.0f,
                    41 / 128.0f);
            numberBackgrounds[i] = new Texture(ORB_AND_NUMBERS, (i * 8) / 128.0f, 41 / 128.0f, (i * 8 + 8) / 128.0f,
                    50 / 128.0f);
        }
    }

    protected void drawOrbAndLevelNumbers(MatrixStack matrices, int level, boolean available) {
        if (level == 0) return;
        Texture orb;
        if (level <= 10) {
            orb = available ? ORB_BRIGHT_SMALL : ORB_DIM_SMALL;
        } else if (level <= 20) {
            orb = available ? ORB_BRIGHT_MEDIUM : ORB_DIM_MEDIUM;
        } else {
            orb = available ? ORB_BRIGHT_LARGE : ORB_DIM_LARGE;
        }
        ScreenDrawing.texturedRect(matrices, 1, 1, 16, 16, orb, 0xFFFFFFFF);
        int[] digits = MathHelper.getDigits(level);
        for (int i = 0; i < digits.length; i++) {
            ScreenDrawing.texturedRect(matrices, 9 + i * 8, 3, 8, 9, numberForegrounds[digits[i]],
                    available ? 0x00_c8ff8f : 0x00_8c605d);
            ScreenDrawing.texturedRect(matrices, 9 + i * 8, 3, 8, 9, numberBackgrounds[digits[i]],
                    available ? 0x00_2d2102 : 0x00_47352f);
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
        int phrasesX = x + 20;
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
        String powerString = "" + enchantmentPower;
        int phrasesWidthLimit = 86 - this.textRenderer.getWidth(powerString);
        StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer,
                phrasesWidthLimit);
        int t = 6839882;
        if (!available) {
            ScreenDrawing.texturedRect(matrices, x, y, WIDTH, HEIGHT, BACKGROUND_UNAVAILABLE, 0xFFFFFFFF);
            // draw exp orbs
            drawOrbAndLevelNumbers(matrices, enchantmentPower, available);
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
            drawOrbAndLevelNumbers(matrices, enchantmentPower, available);
//            this.drawTexture(matrices, widgetX + 1, j + 15 + 19 * o, 16 * o, 223, 16, 16);
            this.textRenderer.drawTrimmed(stringVisitable, phrasesX, phrasesY, phrasesWidthLimit, t);
//            ScreenDrawing.drawString(matrices, stringVisitable.getString(), phrasesX, 2, 0xFF_000000 + t);
            t = 8453920;
        }
//        this.textRenderer.drawWithShadow(matrices, powerString, (float)(phrasesX + 86 - this.textRenderer.getWidth(powerString)), 9.0f, t);
        if (0 <= mouseX && mouseX <= WIDTH && 0 <= mouseY && mouseY <= HEIGHT) {
            renderTooltip(matrices, x, y, mouseX, mouseY);
        }
//         */
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if (enchantment == null) {
            return;
        }
        tooltip.add(Text.translatable("container.enchant.clue", MiscHelper.getEnchantmentName(enchantment)).formatted(
                Formatting.WHITE));
        // TODO Finish tooltips
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }
}
