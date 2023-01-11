package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.TeleporterControllerEntity;
import com.brainsmash.broken_world.registry.DimensionRegister;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.portal.frame.PortalFrameTester;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TeleporterPlatformGuiDescription extends SyncedGuiDescription {
    private static final Identifier SELECT_MESSAGE = new Identifier("broken_world", "select_button_click");
    private static final int INVENTORY_SIZE = 1;
    private static final int PROPERTY_COUNT = 2;
    private String selectDim;

    public TeleporterPlatformGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(SELECT_MESSAGE, buf -> {
            selectDim = buf.readString();
            context.get((world, pos) -> {
                return true;
            });
        });

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        selectDim = "none";
        BiConsumer<String,WButton> buttonBiConsumer = (s, wButton) -> {
            wButton.setLabel(Text.of(s));
            wButton.setOnClick(() -> {selectDim = s;});
        };
        WBar bar = new WBar(new Identifier(Main.MODID,"textures/gui/small_electric_bar.png"),new Identifier(Main.MODID,"textures/gui/small_electric_bar_filled.png"),0,1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 8, 1,1,1);
        WListPanel<String,WButton> dimList = new WListPanel<>(
                List.of("broken_world:moon","broken_world:metallic","broken_world:lush","broken_world:sulfuric"),
                () -> {
            return new WButton(Text.of(""));
        }, buttonBiConsumer);
        root.add(dimList,0,1,8,3);
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 8, 2);
        WButton select = new WButton(Text.of("√"));
        select.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(SELECT_MESSAGE, buf -> {
                    // Write the lucky number
                    buf.writeString(selectDim);
            });
        });
        root.add(select,8,3);

        root.add(this.createPlayerInventoryPanel(), 0, 4);

        root.validate(this);
    }
}
