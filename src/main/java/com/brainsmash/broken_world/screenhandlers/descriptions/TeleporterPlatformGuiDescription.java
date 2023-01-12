package com.brainsmash.broken_world.screenhandlers.descriptions;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.TeleporterControllerEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.DimensionRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class TeleporterPlatformGuiDescription extends SyncedGuiDescription {
    private static final Identifier SELECT_MESSAGE = new Identifier("broken_world", "select_button_click");
    private static final Identifier NEW_ENTRY = new Identifier("broken_world","new_entry");
    private static final Identifier UNREGIST = new Identifier("broken_world","unregist");
    private static final int INVENTORY_SIZE = 1;
    private static final int PROPERTY_COUNT = 2;
    private String selectDest;

    public TeleporterPlatformGuiDescription(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readBlockPos(),buf);
    }

    public TeleporterPlatformGuiDescription(int syncId, PlayerInventory playerInventory, BlockPos pos, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.world,pos));
        if(getNetworkSide() == NetworkSide.CLIENT) {
            PlayerEntity player = playerInventory.player;
            NbtCompound element = buf.readUnlimitedNbt();
            NbtList list = (NbtList) element.get("teleporterList");
            if (list == null) {
                list = new NbtList();
            }
            boolean flag = true;
            for (NbtElement ele : list) {
                NbtCompound nbt = (NbtCompound) ele;
                if (nbt.getLong("pos") == pos.asLong() && nbt.getString("dimension").equals(world.getDimensionKey().getValue().toString())) {
                    flag = false;
                }
            }
            if (flag) {
                WGridPanel root = new WGridPanel();
                setRootPanel(root);
                root.setSize(150, 175);
                root.setInsets(Insets.ROOT_PANEL);
                root.add(this.createPlayerInventoryPanel(), 0, 4);
                WTextField text = new WTextField();
                root.add(text, 0, 2, 6, 1);
                WButton confirm = new WButton(Text.of("✓"));
                confirm.setOnClick(() -> {
                    ScreenNetworking.of(this, NetworkSide.CLIENT).send(NEW_ENTRY, buffer -> {
                        buffer.writeString(text.getText());
                    });
                    root.remove(text);
                    root.remove(confirm);
                    addComponent(root,element,pos);
                    root.validate(this);
                });
                root.add(confirm, 7, 2, 2, 1);
                root.validate(this);
            } else {
                createGUI(element,pos);
            }
        }
    }

    public TeleporterPlatformGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Main.TELEPORT_PLATFORM_GUI_DESCRIPTION, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context,PROPERTY_COUNT));
        PlayerEntity player = playerInventory.player;
        ScreenNetworking.of(this,NetworkSide.SERVER).receive(UNREGIST,buf -> {
            context.get((world,pos)->{
                NbtCompound element = (NbtCompound) ((EntityDataExtension)player).getData();
                NbtList list = (NbtList) element.get("teleporterList");
                if (list == null) {
                    list = new NbtList();
                }
                for (NbtElement ele : list) {
                    NbtCompound nbt = (NbtCompound) ele;
                    if (nbt.getLong("pos") == pos.asLong() && nbt.getString("dimension").equals(world.getDimensionKey().getValue().toString())) {
                        list.remove(ele);
                        break;
                    }
                }
                element.put("teleporterList",list);
                ((EntityDataExtension)player).setData(element);
                ((ServerPlayerEntity)player).closeHandledScreen();
                return true;
            });
        });
        ScreenNetworking.of(this,NetworkSide.SERVER).receive(SELECT_MESSAGE,buf -> {
            selectDest = buf.readString();
            NbtCompound element = (NbtCompound) ((EntityDataExtension)player).getData();
            NbtList list = (NbtList) element.get("teleporterList");
            if(list == null){
                list = new NbtList();
            }
            for(NbtElement ele:list){
                NbtCompound compound = (NbtCompound) ele;
                if(compound.getString("name").equals(selectDest)){
                    ServerWorld destination = ((ServerWorld)world).getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY,new Identifier(compound.getString("dimension"))));
                    if(destination != null){
                        BlockPos blockPos = BlockPos.fromLong(compound.getLong("pos"));
                        destination.getChunkManager().addTicket(ChunkTicketType.PORTAL,new ChunkPos(blockPos),1,blockPos);
                        if(destination.getBlockState(blockPos).getBlock().equals(BlockRegister.blocks[BlockRegistry.TELEPORT_PLATFORM.ordinal()])){
                            context.get((world,pos)->{
                                if(world.getBlockEntity(pos) instanceof ConsumerBlockEntity blockEntity){
                                    if(blockEntity.getEnergy() == blockEntity.getMaxCapacity()){
                                        ((ServerPlayerEntity) player).teleport(destination, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), player.getYaw(), player.getPitch());
                                        blockEntity.setEnergy(0);
                                        ((ServerPlayerEntity)player).closeHandledScreen();
                                    }
                                }
                                return true;
                            });

                        }else {
                            list.remove(ele);
                            element.put("teleporterList",list);
                            ((EntityDataExtension)player).setData(element);
                            ((ServerPlayerEntity)player).closeHandledScreen();
                        }
                    }else{
                        list.remove(ele);
                        element.put("teleporterList",list);
                        ((EntityDataExtension)player).setData(element);
                        ((ServerPlayerEntity)player).closeHandledScreen();
                    }
                    return;
                }
            }
        });
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(NEW_ENTRY, buf -> {
            String name = buf.readString();
            context.get((world, pos) -> {
                NbtCompound element = (NbtCompound) ((EntityDataExtension)player).getData();
                NbtList list = (NbtList) element.get("teleporterList");
                if(list == null){
                    list = new NbtList();
                }
                NbtCompound nbt = new NbtCompound();
                nbt.putLong("pos", pos.asLong());
                nbt.putString("dimension", world.getDimensionKey().getValue().toString());
                nbt.putString("name", name);
                list.add(nbt);
                element.put("teleporterList", list);
                ((EntityDataExtension) player).setData(element);
                return true;
            });
        });
    }

    public void addComponent(WGridPanel root,NbtCompound element,BlockPos pos){
        selectDest = "none";
        BiConsumer<String, WButton> buttonBiConsumer = (s, wButton) -> {
            wButton.setLabel(Text.of(s));
            wButton.setOnClick(() -> {
                selectDest = s;
            });
        };
        WBar bar = new WBar(new Identifier(Main.MODID, "textures/gui/small_electric_bar.png"), new Identifier(Main.MODID, "textures/gui/small_electric_bar_filled.png"), 0, 1);
        bar.setProperties(propertyDelegate);
        root.add(bar, 8, 1, 1, 1);

        List<String> destinations = new ArrayList<>();
        NbtList list = (NbtList) element.get("teleporterList");
        if (list == null) {
            list = new NbtList();
        }
        for (NbtElement ele : list) {
            NbtCompound nbt = (NbtCompound) ele;
            if (!(nbt.getLong("pos") == pos.asLong() && nbt.getString("dimension").equals(world.getDimensionKey().getValue().toString()))) {
                destinations.add(nbt.getString("name"));
            }
        }

        WListPanel<String, WButton> destList = new WListPanel<>(destinations, () -> {
            return new WButton(Text.of(""));
        }, buttonBiConsumer);
        root.add(destList, 0, 1, 8, 3);
        WButton unregist = new WButton(Text.of("×"));
        unregist.setOnClick(()->{
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(UNREGIST, buf -> {
            });
        });
        root.add(unregist,8,2,1,1);
        WButton select = new WButton(Text.of("√"));
        select.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(SELECT_MESSAGE, buf -> {
                buf.writeString(selectDest);
            });
        });
        root.add(select, 8, 3);
    }

    public void createGUI(NbtCompound element,BlockPos pos) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 175);
        root.setInsets(Insets.ROOT_PANEL);
        root.add(this.createPlayerInventoryPanel(), 0, 4);
        addComponent(root,element,pos);
        root.validate(this);
    }

}
