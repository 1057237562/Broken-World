package com.brainsmash.broken_world.screenhandlers.descriptions;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.gui.widgets.WFluidWidget;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class ThermalGeneratorGuiDescription extends SyncedGuiDescription {

    private static final int INVENTORY_SIZE = 2;
    private static final int PROPERTY_COUNT = 4;

    public ThermalGeneratorGuiDescription(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(syncId,playerInventory,ScreenHandlerContext.create(playerInventory.player.world,buf.readBlockPos()));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        WBar bar = new WBar(new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar.png"),new Identifier(Main.MODID,"textures/gui/horizontal_electric_bar_filled.png"),0,1, WBar.Direction.RIGHT);
        bar.setProperties(propertyDelegate);
        root.add(bar, 6, 2,2,1);
        WItemSlot outputSlot = WItemSlot.of(blockInventory, 0);
        root.add(outputSlot, 4, 1);
        WItemSlot powerSource = WItemSlot.of(blockInventory, 1);
        root.add(powerSource, 4, 3);
        WFluidWidget fluidWidget = new WFluidWidget(FluidVolume.fromTag(buf.readNbt()),propertyDelegate,2,3);
        root.add(fluidWidget,2,1,1,3);
        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }

    public ThermalGeneratorGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.THERMAL_GENERATOR_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));

    }
}
