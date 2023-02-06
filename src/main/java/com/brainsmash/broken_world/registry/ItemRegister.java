package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.items.*;
import com.brainsmash.broken_world.items.armor.material.KineticMaterial;
import com.brainsmash.broken_world.items.weapons.ammo.HeavyAmmo;
import com.brainsmash.broken_world.items.weapons.ammo.LightAmmo;
import com.brainsmash.broken_world.items.weapons.ammo.SniperAmmo;
import com.brainsmash.broken_world.items.weapons.guns.*;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.brainsmash.broken_world.Main.MODID;
import static com.brainsmash.broken_world.registry.FluidRegister.still_fluid;

public class ItemRegister {
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "itemgroup"),
            () -> new ItemStack(BlockRegister.blockitems[0]));

    public static final ArmorMaterial[] armorMaterials = {new KineticMaterial()};

    public static final Item[] bucket_item = {
            new BucketItem(still_fluid[0],
                    new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
            new BucketItem(still_fluid[1],
                    new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
            new BucketItem(still_fluid[2],
                    new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
            new BucketItem(still_fluid[3],
                    new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
    };

    public static final Item[] items = {
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new BreathingEPP(new FabricItemSettings().maxCount(1).group(ITEM_GROUP)),
            new EmergencyTeleporter(new FabricItemSettings().group(ITEM_GROUP)),
            new AdvancedEnderPearl(new FabricItemSettings().maxCount(16).group(ITEM_GROUP)),
            new Boulder(new FabricItemSettings().maxCount(32).group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new HyperPocket(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new Pistol(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new LightAmmo(new FabricItemSettings().group(ITEM_GROUP)),
            new SMG(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new HeavyAmmo(new FabricItemSettings().group(ITEM_GROUP)),
            new Rifle(new FabricItemSettings().group(ITEM_GROUP)),
            new SniperAmmo(new FabricItemSettings().group(ITEM_GROUP)),
            new SniperRifle(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new HyperSpear(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.FEET,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),

    };

    public static final String[] itemnames = {
            "titanium_ingot",
            "tungsten_ingot",
            "oxygen_generator_pack",
            "emergency_teleporter",
            "advanced_ender_pearl",
            "boulder",
            "silicon",
            "aa_battery",
            "lithium_ingot",
            "zinc_ingot",
            "copper_coil",
            "plastic_plate",
            "memory_chip",
            "steel_ingot",
            "asphalt",
            "motor",
            "iron_plate",
            "magnet_ingot",
            "spring_roll",
            "mesh",
            "hyper_pocket",
            "pistol",
            "light_ammo",
            "smg",
            "heavy_ammo",
            "rifle",
            "sniper_ammo",
            "sniper_rifle",
            "hyper_spear",
            "kinetic_helmet",
            "kinetic_suit",
            "kinetic_leg",
            "kinetic_boots"
    };

    public static final Item[] guns = {
            items[ItemRegistry.PISTOL.ordinal()],
            items[ItemRegistry.SMG.ordinal()],
            items[ItemRegistry.RIFLE.ordinal()],
            items[ItemRegistry.SNIPER_RIFLE.ordinal()]
    };

    public static void registItem() {
        for (int i = 0; i < items.length; i++) {
            Registry.register(Registry.ITEM, new Identifier(MODID, itemnames[i]), items[i]);
        }
    }

    public static void registItemClientSide() {
        for (Item gun : guns) {
            ModelPredicateProviderRegistry.register(gun, new Identifier("aiming"), (stack, world, entity, seed) -> {
                if (entity == null) {
                    return 0.0f;
                }
                return entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f;
            });
        }
    }
}
