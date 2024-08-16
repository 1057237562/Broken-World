package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.blocks.fluid.storage.FluidSlot;
import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.blocks.render.FluidVariantRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.function.Function;

public class WFluidWidget extends WWidget {

    private SingleFluidStorage<FluidVariant> inv;
    private PropertyDelegate propertyDelegate;
    private int fieldId = -1;

    private Texture background;
    private Texture scale;

    private Function<Integer, Boolean> onClick;

    public void setOnClick(Function<Integer, Boolean> onClick) {
        this.onClick = onClick;
    }

    public WFluidWidget(FluidSlot fluidSlot, PropertyDelegate delegate, Identifier bg, Identifier scale, int field, int capacity) {
        propertyDelegate = delegate;
        fieldId = field;
        background = new Texture(bg);
        this.scale = new Texture(scale);
        inv = new SingleFluidStorage<FluidVariant>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return fluidSlot.variant;
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return propertyDelegate.get(capacity);
            }
        };
        inv.variant = fluidSlot.variant;
        inv.amount = fluidSlot.amount;
    }

    public WFluidWidget(SingleFluidStorage<FluidVariant> fluidInv, PropertyDelegate delegate, Identifier bg, Identifier scale) {
        inv = fluidInv;
        propertyDelegate = delegate;
        background = new Texture(bg);
        this.scale = new Texture(scale);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(matrices, x - 1, y - 1, 18, 50, background, 0xFFFFFFFF);

        int x0 = x;
        int y0;
        if (fieldId == -1) {
            y0 = (int) (y + 48 - 48 * (double) inv.amount / (double) inv.getCapacity());
        } else {
            y0 = (int) (y + 48 - 48 * (double) propertyDelegate.get(fieldId) / (double) inv.getCapacity());
        }
        int x1 = x + 16;
        int y1 = y + 48;
        FluidVariantRenderer.INSTANCE.renderGuiRectangle(inv.variant, x0, y0, x1, y1);
        ScreenDrawing.texturedRect(matrices, x, y, 16, 48, scale, 0xFFFFFFFF);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (onClick != null) return InputResult.of(onClick.apply(button));
        return super.onMouseDown(x, y, button);
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        super.addTooltip(tooltip);
        if (fieldId == -1) {
            tooltip.add(
                    Text.of(inv.amount * 1000 / FluidConstants.BUCKET + " mB/" + inv.getCapacity() * 1000 / FluidConstants.BUCKET + " mB"));
        } else {
            tooltip.add(Text.of(propertyDelegate.get(
                    fieldId) * 1000 / FluidConstants.BUCKET + " mB/" + inv.getCapacity() * 1000 / FluidConstants.BUCKET + " mB"));
        }

    }

    public void renderFluidSprite(MatrixStack matrices, int x, int y, int width, int height, Sprite sprite, int color) {
        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int a = (color >>> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        RenderSystem.setShaderColor(r / 255f, g / 255f, b / 255f, a / 255f);
        for (int i = 0; i < height / width; i++) {
            drawTexturedQuad(matrices.peek().getPositionMatrix(), x, x + width, y + height - (i + 1) * width,
                    y + height - i * width, 0, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
        }
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x, x + width, y, y + height % width, 0, sprite.getMinU(),
                sprite.getMaxU(),
                sprite.getMaxV() - (sprite.getMaxV() - sprite.getMinV()) * (height % width / (float) width),
                sprite.getMaxV());
        RenderSystem.disableBlend();
    }

    private static void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).texture(u0, v1).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).texture(u1, v0).next();
        bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).texture(u0, v0).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }
}
