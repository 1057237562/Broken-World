package com.brainsmash.broken_world.gui.widgets;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class WFluidWidget extends WWidget {

    private FixedFluidInv inv;
    private PropertyDelegate propertyDelegate;
    private int fieldId = -1;
    private int capacityId = -1;

    private Texture background;
    private Texture scale;

    private Function<Integer, Boolean> onClick;
    private int index = 0;

    public void setOnClick(Function<Integer, Boolean> onClick) {
        this.onClick = onClick;
    }

    public WFluidWidget(FluidVolume fluidVolume, PropertyDelegate delegate, Identifier bg, Identifier scale, int field, int capacity) {
        inv = new SimpleFixedFluidInv(1, FluidAmount.BUCKET);
        inv.forceSetInvFluid(0, fluidVolume);
        propertyDelegate = delegate;
        fieldId = field;
        capacityId = capacity;
        background = new Texture(bg);
        this.scale = new Texture(scale);
    }

    public WFluidWidget(FixedFluidInv fluidInv, PropertyDelegate delegate, Identifier bg, Identifier scale, int index) {
        inv = fluidInv;
        propertyDelegate = delegate;
        background = new Texture(bg);
        this.scale = new Texture(scale);
        this.index = index;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(matrices, x - 1, y - 1, 18, 50, background, 0xFFFFFFFF);

        double x0 = x;
        double y0;
        FluidVolume volume = inv.getInvFluid(index);
        if (fieldId == -1 || capacityId == -1) {
            y0 = y + 48 - 48 * (double) volume.amount().as1620() / (double) inv.getMaxAmount_F(index).as1620();
        } else {
            y0 = y + 48 - 48 * (double) propertyDelegate.get(fieldId) / (double) propertyDelegate.get(capacityId);
        }
        double x1 = x + 16;
        double y1 = y + 48;
        inv.getInvFluid(index).renderGuiRect(x0, y0, x1, y1);

        ScreenDrawing.texturedRect(matrices, x, y, 16, 48, scale, 0xFFFFFFFF);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (onClick != null) return InputResult.of(onClick.apply(button));
        return super.onMouseDown(x, y, button);
    }
}
