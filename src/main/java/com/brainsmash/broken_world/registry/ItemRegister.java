package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.items.*;
import com.brainsmash.broken_world.items.armor.material.KineticMaterial;
import com.brainsmash.broken_world.items.electrical.BatteryItem;
import com.brainsmash.broken_world.items.magical.Rune;
import com.brainsmash.broken_world.items.magical.Wand;
import com.brainsmash.broken_world.items.magical.enums.RuneEnum;
import com.brainsmash.broken_world.items.weapons.HoeItem;
import com.brainsmash.broken_world.items.weapons.ammo.HeavyAmmo;
import com.brainsmash.broken_world.items.weapons.ammo.LightAmmo;
import com.brainsmash.broken_world.items.weapons.ammo.SniperAmmo;
import com.brainsmash.broken_world.items.weapons.guns.*;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import com.brainsmash.broken_world.registry.enums.ToolRegistry;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

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
            new BucketItem(still_fluid[4],
                    new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
            new BucketItem(still_fluid[5],
                    new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
    };

    public static final Item[] items = {
            // 0
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new BreathingEPP(new FabricItemSettings().maxCount(1).group(ITEM_GROUP)),
            new EmergencyTeleporter(new FabricItemSettings().group(ITEM_GROUP)),
            new AdvancedEnderPearl(new FabricItemSettings().maxCount(16).group(ITEM_GROUP)),
            new Boulder(new FabricItemSettings().maxCount(32).group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new BatteryItem(new FabricItemSettings().group(ITEM_GROUP).maxCount(1), 500, false),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            // 10
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
            // 20
            new HyperPocket(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new Pistol(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new LightAmmo(new FabricItemSettings().group(ITEM_GROUP)),
            new SMG(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new HeavyAmmo(new FabricItemSettings().group(ITEM_GROUP)),
            new Rifle(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new SniperAmmo(new FabricItemSettings().group(ITEM_GROUP)),
            new SniperRifle(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new HyperSpear(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            // 30
            new ArmorItem(armorMaterials[0], EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new ArmorItem(armorMaterials[0], EquipmentSlot.FEET,
                    new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new Pistol(new FabricItemSettings().group(ITEM_GROUP).maxCount(1)),
            new CoordinateCard(new FabricItemSettings().group(ITEM_GROUP).maxCount(1), "broken_world:metallic"),
            new CoordinateCard(new FabricItemSettings().group(ITEM_GROUP).maxCount(1), "broken_world:sulfuric"),
            new CoordinateCard(new FabricItemSettings().group(ITEM_GROUP).maxCount(1), "broken_world:lush"),
            new CoordinateCard(new FabricItemSettings().group(ITEM_GROUP).maxCount(1), "broken_world:floating"),
            new CoordinateCard(new FabricItemSettings().group(ITEM_GROUP).maxCount(1), "broken_world:aurora"),
            new Wand(new FabricItemSettings().group(ITEM_GROUP), Main.ROOKIE_WAND_SCREEN_HANDLER, 1),
            // 40
            new Wand(new FabricItemSettings().group(ITEM_GROUP), Main.EXPERT_WAND_SCREEN_HANDLER, 3),
            new Wand(new FabricItemSettings().group(ITEM_GROUP), Main.MASTER_WAND_SCREEN_HANDLER, 6),
            new Wand(new FabricItemSettings().group(ITEM_GROUP), Main.GRANDMASTER_WAND_SCREEN_HANDLER, 9),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.GALACTICA),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.EARTH),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.FIRE),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.LIQUID),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.WIND),
            // 50
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.MOUNTAINOUS),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.MARSH),
            new Rune(new FabricItemSettings().group(ITEM_GROUP), RuneEnum.THUNDER),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new DensityMeter(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP).maxCount(1).recipeRemainder(Items.BOWL)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            new Item(new FabricItemSettings().group(ITEM_GROUP)),
            // 60
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
            new BatteryItem(new FabricItemSettings().group(ITEM_GROUP), 1500, true),
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
            "g17",
            "light_ammo",
            "smg",
            "heavy_ammo",
            "mk144",
            "sniper_ammo",
            "hass_03",
            "hyper_spear",
            "kinetic_helmet",
            "kinetic_suit",
            "kinetic_leg",
            "kinetic_boots",
            "qs_093",
            "metallic_card",
            "sulfuric_card",
            "lush_card",
            "floating_card",
            "aurora_card",
            "rookie_wand",
            "expert_wand",
            "master_wand",
            "grandmaster_wand",
            "kyanite_shard",
            "blank_rune",
            "galactica_rune",
            "earth_rune",
            "fire_rune",
            "liquid_rune",
            "wind_rune",
            "mountainous_rune",
            "marsh_rune",
            "thunder_rune",
            "tin_ingot",
            "density_meter",
            "bowl_of_latex",
            "rubber",
            "raw_tin",
            "centrifuge_axle",
            "heating_wire",
            "gas_tank",
            "oxygen_tank",
            "steel_plate",
            "circuit_board",
            "bronze_ingot",
            "tinplate",
            "chip",
            "sulfur",
            "raw_aluminum",
            "aluminum_ingot",
            "lead_ingot",
            "la_battery"
    };

    public static final Item[] guns = {
            items[ItemRegistry.G17.ordinal()],
            items[ItemRegistry.SMG.ordinal()],
            items[ItemRegistry.MK144.ordinal()],
            items[ItemRegistry.HASS_03.ordinal()],
            items[ItemRegistry.QS_093.ordinal()]
    };

    public static final String[] tools = {
            "copper",
            "tungsten"
    };

    public static final ArrayList<Item> toolsItem = new ArrayList<>();

    public static void registItem() {
        for (int i = 0; i < tools.length; i++) {
            String materialName = tools[i];
            ToolMaterial material = ToolRegistry.values()[i];
            toolsItem.add(Registry.register(Registry.ITEM, new Identifier(MODID, materialName + "_pickaxe"),
                    new PickaxeItem(material, 1, -2.8F, new FabricItemSettings().group(ItemGroup.TOOLS))));
            toolsItem.add(Registry.register(Registry.ITEM, new Identifier(MODID, materialName + "_axe"),
                    new AxeItem(material, 6.0F, -3.1F, new FabricItemSettings().group(ItemGroup.TOOLS))));
            toolsItem.add(Registry.register(Registry.ITEM, new Identifier(MODID, materialName + "_sword"),
                    new SwordItem(material, 3, -2.4F, new FabricItemSettings().group(ItemGroup.COMBAT))));
            toolsItem.add(Registry.register(Registry.ITEM, new Identifier(MODID, materialName + "_shovel"),
                    new ShovelItem(material, 1.5F, -3.0F, new FabricItemSettings().group(ItemGroup.TOOLS))));
            toolsItem.add(Registry.register(Registry.ITEM, new Identifier(MODID, materialName + "_hoe"),
                    new HoeItem(material, -2, -1.0F, new FabricItemSettings().group(ItemGroup.TOOLS))));
        }

        for (int i = 0; i < items.length; i++) {
            Registry.register(Registry.ITEM, new Identifier(MODID, itemnames[i]), items[i]);
        }

        /*Registry.register(Registry.ITEM, new Identifier("minecraft", "bowl"),
                new BowlItem(new FabricItemSettings().group(ITEM_GROUP)));*/

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
