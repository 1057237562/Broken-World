package com.brainsmash.broken_world.gui.widgets;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.ColliderControllerBlockEntity;
import com.brainsmash.broken_world.util.Vec2i;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Identifier;

public class WColliderStatus extends WWidget {
    private static final int COIL_ICON_WIDTH = 4;
    private final int propIndex;
    private int gridWidth = 18;
    private static final Identifier SIDE_ICONS = new Identifier(Main.MODID, "textures/gui/collider/side.png");
    private static final Identifier CORNER_ICONS = new Identifier(Main.MODID, "textures/gui/collider/corner.png");
    private PropertyDelegate properties;

    public WColliderStatus(int colorIndex) {
        propIndex = colorIndex;
    }

    public WColliderStatus(int colorIndex, int gridWidth) {
        propIndex = colorIndex;
        this.gridWidth = gridWidth;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(DrawContext content, int x, int y, int mouseX, int mouseY) {
        final int l = ColliderControllerBlockEntity.COLLIDER_SIDE_LENGTH;
        final int w = COIL_ICON_WIDTH;
        Vec2i[] directions = {
                Vec2i.NEGATIVE_Y,
                Vec2i.POSITIVE_X,
                Vec2i.POSITIVE_Y,
                Vec2i.NEGATIVE_X
        };
        Vec2i.Mutable p = new Vec2i.Mutable(x + gridWidth / 2 - l / 2 * w - w / 2,
                y + gridWidth / 2 + l / 2 * w - w / 2);
        for (int i = 0; i < 4; i++) {
            ScreenDrawing.texturedRect(content, p.getX(), p.getY(), w, w, CORNER_ICONS, i * 0.25f, 0f,
                    i * 0.25f + 0.25f, 1f, properties.get(propIndex + i * (l - 1)));
            for (int j = 0; j < l - 2; j++) {
                p.move(directions[i].mul(w));
                ScreenDrawing.texturedRect(content, p.getX(), p.getY(), w, w, SIDE_ICONS, i % 2 * 0.5f, 0f,
                        i % 2 * 0.5f + 0.5f, 1f, properties.get(propIndex + i * (l - 1) + 1 + j));
            }
            p.move(directions[i].mul(w));
        }
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void validate(GuiDescription host) {
        super.validate(host);
        if (properties == null) properties = host.getPropertyDelegate();
    }
}
