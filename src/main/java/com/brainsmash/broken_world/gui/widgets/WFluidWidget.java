package com.brainsmash.broken_world.gui.widgets;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Identifier;

public class WFluidWidget extends WWidget {

    private FluidVolume volume;
    private PropertyDelegate propertyDelegate;
    private int fieldId;
    private int capacityId;

    private Texture background;
    private Texture scale;

    public WFluidWidget(FluidVolume fluidVolume, PropertyDelegate delegate,Identifier bg,Identifier scale, int field, int capacity){
        volume = fluidVolume;
        propertyDelegate = delegate;
        fieldId = field;
        capacityId = capacity;
        background = new Texture(bg);
        this.scale = new Texture(scale);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(matrices,x-1,y-1,18,50,background,0xFFFFFFFF);

        double x0 = x;
        double y0 = y + 48 - 48 * (double)propertyDelegate.get(fieldId) / (double)propertyDelegate.get(capacityId);
        double x1 = x + 16;
        double y1 = y + 48;
        volume.renderGuiRect(x0, y0, x1, y1);

        ScreenDrawing.texturedRect(matrices,x,y,16,48,scale,0xFFFFFFFF);
    }
}
