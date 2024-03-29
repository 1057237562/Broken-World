package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.TeleporterControllerBlockEntity;
import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TeleporterControllerGuiDescription extends SyncedGuiDescription {
    private static final Identifier SELECT_MESSAGE = new Identifier("broken_world", "select_button_click");
    private static final int INVENTORY_SIZE = 1;
    private static final int PROPERTY_COUNT = 2;
    private String selectDim;

    public TeleporterControllerGuiDescription(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readBlockPos(), buf);
    }

    public TeleporterControllerGuiDescription(int syncId, PlayerInventory playerInventory, BlockPos pos, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.world, pos));
        if (getNetworkSide() == NetworkSide.CLIENT) {
            ((EntityDataExtension) playerInventory.player).setData(buf.readNbt());
            WGridPanel root = new WGridPanel();
            setRootPanel(root);
            root.setSize(150, 175);
            root.setInsets(Insets.ROOT_PANEL);
            selectDim = "none";
            BiConsumer<String, WButton> buttonBiConsumer = (s, wButton) -> {
                wButton.setLabel(Text.translatable(s));
                wButton.setOnClick(() -> {
                    selectDim = s;
                });
            };
            WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                    new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
            bar.setProperties(propertyDelegate);
            root.add(bar, 8, 1, 1, 1);
            ArrayList<String> dimensionList = new ArrayList<>(List.of("broken_world:moon"));

            NbtCompound nbtCompound = (NbtCompound) ((EntityDataExtension) playerInventory.player).getData();
            NbtList list = nbtCompound.getList("dimension", NbtElement.COMPOUND_TYPE);
            if (list != null) {
                for (NbtElement element : list) {
                    if (element instanceof NbtCompound compound) {
                        dimensionList.add(compound.getString("key"));
                    }
                }
            }


            WListPanel<String, WButton> dimList = new WListPanel<>(dimensionList, () -> new WButton(Text.of("")),
                    buttonBiConsumer);
            root.add(dimList, 0, 1, 8, 5);
            WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
            root.add(itemSlot, 8, 2);
            WButton select = new WButton(Text.of("✓"));
            select.setOnClick(() -> {
                ScreenNetworking.of(this, NetworkSide.CLIENT).send(SELECT_MESSAGE, buf1 -> {
                    // Write the lucky number
                    buf1.writeString(selectDim);
                });
            });
            root.add(select, 8, 5);

            root.add(this.createPlayerInventoryPanel(), 0, 6);

            root.validate(this);
        }
    }

    public TeleporterControllerGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, syncId, playerInventory,
                getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context, PROPERTY_COUNT));
        if (getNetworkSide() == NetworkSide.SERVER) {
            ScreenNetworking.of(this, NetworkSide.SERVER).receive(SELECT_MESSAGE, buf -> {
                selectDim = buf.readString();
                
                context.get((world, pos) -> {
                    TeleporterControllerBlockEntity entity = (TeleporterControllerBlockEntity) world.getBlockEntity(
                            pos);
                    if (entity.getEnergy() >= DimensionRegister.dimensionEnergyCost.get(selectDim)) {
                        entity.increaseEnergy(-DimensionRegister.dimensionEnergyCost.get(selectDim));
                        BlockPos baseblock = CustomPortalHelper.getClosestFrameBlock(world, pos);
                        Block oldblock = world.getBlockState(baseblock).getBlock();
                        PortalLink oldlink = CustomPortalApiRegistry.getPortalLinkFromBase(oldblock);
                        BlockPos portalbase = baseblock.add(0, 1, 0);
                        world.breakBlock(portalbase, false);
                        PortalLink link = DimensionRegister.dimensions.get(selectDim);
                        Block linkblock = Registry.BLOCK.get(link.block);
                        if (replacePortalBlock(oldlink, oldblock, world, portalbase, linkblock)) {
                            if (link != null && link.canLightInDim(world.getRegistryKey().getValue())) {
                                createPortal(link, linkblock, world, portalbase);
                            }
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
            BiConsumer<String, WButton> buttonBiConsumer = (s, wButton) -> {
                wButton.setLabel(Text.translatable(s));
                wButton.setOnClick(() -> {
                    selectDim = s;
                });
            };
            WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"),
                    new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
            bar.setProperties(propertyDelegate);
            root.add(bar, 8, 1, 1, 1);
            ArrayList<String> dimensionList = new ArrayList<>(List.of("broken_world:moon"));

            NbtCompound nbtCompound = (NbtCompound) ((EntityDataExtension) playerInventory.player).getData();
            NbtList list = nbtCompound.getList("dimension", NbtElement.COMPOUND_TYPE);
            if (list != null) {
                for (NbtElement element : list) {
                    if (element instanceof NbtCompound compound) {
                        dimensionList.add(compound.getString("key"));
                    }
                }
            }


            WListPanel<String, WButton> dimList = new WListPanel<>(dimensionList, () -> new WButton(Text.of("")),
                    buttonBiConsumer);
            root.add(dimList, 0, 1, 8, 3);
            WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
            root.add(itemSlot, 8, 2);
            WButton select = new WButton(Text.of("✓"));
            select.setOnClick(() -> {
                ScreenNetworking.of(this, NetworkSide.CLIENT).send(SELECT_MESSAGE, buf -> {
                    // Write the lucky number
                    buf.writeString(selectDim);
                });
            });
            root.add(select, 8, 3);

            root.add(this.createPlayerInventoryPanel(), 0, 4);

            root.validate(this);
        }
    }

    private static boolean replacePortalBlock(PortalLink oldlink, Block oldBlock, World world, BlockPos portalPos, Block replacement) {
        Optional<PortalFrameTester> optional = oldlink.getFrameTester().createInstanceOfPortalFrameTester().getNewPortal(
                world, portalPos, Direction.Axis.X, oldBlock);
        //is valid frame, and is correct size(if applicable)
        if (optional.isPresent()) {
            if (optional.get().isRequestedSize(oldlink.forcedWidth, oldlink.forcedHeight)) {
                PortalFrameTester pft = optional.get();
                BlockLocating.Rectangle rectangle = pft.getRectangle();
                for (int i = -1; i <= rectangle.width; i++) {
                    for (int j = -1; j <= rectangle.height; j++) {
                        if (CustomPortalHelper.isInstanceOfPortalFrame(world,
                                rectangle.lowerLeft.offset(pft.getAxis1(), i).offset(pft.getAxis2(), j))) {
                            world.setBlockState(rectangle.lowerLeft.offset(pft.getAxis1(), i).offset(pft.getAxis2(), j),
                                    replacement.getDefaultState());
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
        Optional<PortalFrameTester> optional = link.getFrameTester().createInstanceOfPortalFrameTester().getNewPortal(
                world, portalPos, Direction.Axis.X, foundationBlock);

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
