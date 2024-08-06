package com.brainsmash.broken_world;

import com.brainsmash.broken_world.blocks.multiblock.MultiblockUtil;
import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.weapons.guns.GunItem;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.EntityRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.screens.cotton.*;
import com.brainsmash.broken_world.util.BonusHelper;
import com.brainsmash.broken_world.util.EntityHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityPose;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static com.brainsmash.broken_world.Main.MODID;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    public static final String CATEGORY = "key.category.broken_world";

    public static KeyBinding crawlKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.broken_world.crawl", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY));

    public static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.broken_world.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY));

    public static KeyBinding dismountKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.broken_world.dismount", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY));
    public static boolean shading = true;

    @Override
    public void onInitializeClient() {
        HandledScreens.register(Main.TELEPORTER_CONTROLLER_SCREEN_HANDLER_TYPE, TeleporterControllerScreen::new);
        HandledScreens.register(Main.BATTERY_GUI_DESCRIPTION, BatteryScreen::new);
        HandledScreens.register(Main.GENERATOR_GUI_DESCRIPTION, GeneratorScreen::new);
        HandledScreens.register(Main.CRUSHER_GUI_DESCRIPTION, CrusherScreen::new);
        HandledScreens.register(Main.SIFTER_GUI_DESCRIPTION, SifterScreen::new);
        HandledScreens.register(Main.MINER_GUI_DESCRIPTION, MinerScreen::new);
        HandledScreens.register(Main.TELEPORT_PLATFORM_GUI_DESCRIPTION, TeleportPlatformScreen::new);
        HandledScreens.register(Main.THERMAL_GENERATOR_GUI_DESCRIPTION, ThermalGeneratorScreen::new);
        HandledScreens.register(Main.PUMP_GUI_DESCRIPTION, PumpScreen::new);
        HandledScreens.register(Main.ADVANCED_FURNACE_GUI_DESCRIPTION, AdvancedFurnaceScreen::new);
        HandledScreens.register(Main.FABRICATOR_GUI_DESCRIPTION, FabricatorScreen::new);
        HandledScreens.register(Main.GAS_COLLECTOR_GUI_DESCRIPTION, GasCollectorScreen::new);
        HandledScreens.register(Main.REFINERY_GUI_DESCRIPTION, RefineryScreen::new);
        HandledScreens.register(Main.CENTRIFUGE_GUI_DESCRIPTION, CentrifugeScreen::new);
        HandledScreens.register(Main.COMPRESSOR_GUI_DESCRIPTION, CompressorScreen::new);
        HandledScreens.register(Main.ASSEMBLER_GUI_DESCRIPTION, AssemblerScreen::new);
        HandledScreens.register(Main.EXTRACTOR_GUI_DESCRIPTION, ExtractorScreen::new);
        HandledScreens.register(Main.WEAPONRY_GUI_DESCRIPTION, WeaponryScreen::new);
        HandledScreens.register(Main.REACTION_KETTLE_GUI_DESCRIPTION, ReactionKettleScreen::new);
        HandledScreens.register(Main.ELECTROLYZER_GUI_DESCRIPTION, ElectrolyzerScreen::new);
        HandledScreens.register(Main.COLLIDER_CONTROLLER_GUI_DESCRIPTION, ColliderControllerScreen::new);
        HandledScreens.register(Main.INFUSION_TABLE_GUI_DESCRIPTION, InfusingTableScreen::new);

        HandledScreens.register(Main.ROOKIE_WAND_SCREEN_HANDLER, WandScreen::new);
        HandledScreens.register(Main.EXPERT_WAND_SCREEN_HANDLER, WandScreen::new);
        HandledScreens.register(Main.MASTER_WAND_SCREEN_HANDLER, WandScreen::new);
        HandledScreens.register(Main.GRANDMASTER_WAND_SCREEN_HANDLER, WandScreen::new);


        BlockRegister.registBlocksClientSide();
        EntityRegister.registEntitiesClientSide();
        ItemRegister.registItemClientSide();

        MultiblockUtil.registMultiblockClientSide();

        FluidRegister.RegistFluidClientSide();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (crawlKey.isPressed()) {
                if (player.world.isSpaceEmpty(player,
                        EntityHelper.calculateBoundsForPose(player, EntityPose.SWIMMING).contract(1.0E-7))) {
                    player.setPose(EntityPose.SWIMMING);
                    ((PlayerDataExtension) player).forceSetFlag(2, true);
                }
                ClientPlayNetworking.send(new Identifier(MODID, "crawl_key_hold"), PacketByteBufs.create());
            }
            if (client.options.jumpKey.isPressed()) {
                if (!player.getAbilities().flying) {
                    if (player instanceof EntityDataExtension dataExtension) {
                        if (dataExtension.getData() instanceof NbtCompound nbtCompound) {
                            if (BonusHelper.getBoolean(nbtCompound, "jet")) {
                                if (player.getVelocity().y < 0.7f)
                                    player.addVelocity(0, Math.min(0.7f - player.getVelocity().y, 0.3), 0);
                                player.fallDistance = 0;
                            }
                        }
                    }
                }
                ClientPlayNetworking.send(new Identifier(MODID, "jump_key_hold"), PacketByteBufs.create());
            }
            if (client.options.sneakKey.isPressed()) {
                ClientPlayNetworking.send(new Identifier(MODID, "sneak_key_hold"), PacketByteBufs.create());
            }
            if (dismountKey.wasPressed()) {
                ClientPlayNetworking.send(new Identifier(MODID, "dismount_key_press"), PacketByteBufs.create());
            }
            while (reloadKey.wasPressed()) {
                ItemStack stack = player.getMainHandStack();
                if (stack.getItem() instanceof GunItem gunItem) {
                    gunItem.reload(stack);
                    ClientPlayNetworking.send(new Identifier(MODID, "reload_key_press"), PacketByteBufs.create());
                }
            }
        });
    }
}
