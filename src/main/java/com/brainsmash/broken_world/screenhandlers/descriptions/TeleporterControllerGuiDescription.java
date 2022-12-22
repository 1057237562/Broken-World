package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
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

public class TeleporterControllerGuiDescription extends SyncedGuiDescription {
    private static final Identifier SELECT_MESSAGE = new Identifier("broken_world", "select_button_click");
    private static final int INVENTORY_SIZE = 1;
    private String selectDim;

    public TeleporterControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(SELECT_MESSAGE, buf -> {
            selectDim = buf.readString();
            context.get((world, pos) -> {
                BlockPos baseblock = CustomPortalHelper.getClosestFrameBlock(world,pos);
                Block oldblock = world.getBlockState(baseblock).getBlock();
                PortalLink oldlink = CustomPortalApiRegistry.getPortalLinkFromBase(oldblock);
                BlockPos portalbase = baseblock.add(0, 1, 0);
                world.breakBlock(portalbase, false);
                PortalLink link = Main.dimensions.get(selectDim);
                Block linkblock = Registry.BLOCK.get(link.block);
                if(replacePortalBlock(oldlink,oldblock,world,portalbase,linkblock)) {
                    if (link != null && link.canLightInDim(world.getRegistryKey().getValue())) {
                        createPortal(link, linkblock, world, portalbase);
                    }
                }
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

    private static boolean replacePortalBlock(PortalLink oldlink,Block oldBlock,World world,BlockPos portalPos,Block replacement){
        Optional<PortalFrameTester> optional = oldlink.getFrameTester().createInstanceOfPortalFrameTester().getNewPortal(world, portalPos, Direction.Axis.X, oldBlock);
        //is valid frame, and is correct size(if applicable)
        if (optional.isPresent()) {
            if (optional.get().isRequestedSize(oldlink.forcedWidth, oldlink.forcedHeight)) {
                PortalFrameTester pft = optional.get();
                BlockLocating.Rectangle rectangle = pft.getRectangle();
                for (int i = -1; i <= rectangle.width; i++) {
                    for (int j = -1; j <= rectangle.height; j++) {
                        if (CustomPortalHelper.isInstanceOfPortalFrame(world, rectangle.lowerLeft.offset(pft.getAxis1(), i).offset(pft.getAxis2(), j))) {
                            world.setBlockState(rectangle.lowerLeft.offset(pft.getAxis1(), i).offset(pft.getAxis2(), j), replacement.getDefaultState());
                        }
                    }
                }
                return true;
            }
            System.out.println("size not match");
        }
        return false;
    }
    private static boolean createPortal(PortalLink link, Block foundationBlock, World world, BlockPos portalPos) {
        Optional<PortalFrameTester> optional = link.getFrameTester().createInstanceOfPortalFrameTester().getNewPortal(world, portalPos, Direction.Axis.X, foundationBlock);

        //is valid frame, and is correct size(if applicable)
        if (optional.isPresent()) {
            if (optional.get().isRequestedSize(link.forcedWidth, link.forcedHeight)) {

                optional.get().lightPortal(foundationBlock);
            }
            return true;
        }
        return false;
    }
}
