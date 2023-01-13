package com.brainsmash.broken_world.gui.widgets;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;

public class WFluidWidget extends WWidget {

    private FluidVolume volume;
    private PropertyDelegate propertyDelegate;
    private int fieldId;
    private int capacityId;

    public WFluidWidget(FluidVolume fluidVolume, PropertyDelegate delegate,int field,int capacity){
        volume = fluidVolume;
        propertyDelegate = delegate;
        fieldId = field;
        capacityId = capacity;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        double x0 = x;
        double y0 = y + 48 - 48 * (double)propertyDelegate.get(fieldId) / (double)propertyDelegate.get(capacityId);
        double x1 = x + 16;
        double y1 = y + 48;
        volume.renderGuiRect(x0, y0, x1, y1);
    }
}
