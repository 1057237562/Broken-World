package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.items.AdvancedEnderPearl;
import com.brainsmash.broken_world.items.Boulder;
import com.brainsmash.broken_world.items.BreathingEPP;
import com.brainsmash.broken_world.items.EmergencyTeleporter;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

import static com.brainsmash.broken_world.Main.MODID;
import static com.brainsmash.broken_world.registry.FluidRegister.still_fluid;

public class ItemRegister {
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "itemgroup"), new Supplier<>() {
        @Override
        public ItemStack get() {
            return new ItemStack(BlockRegister.blockitems[0]);
        }
    });

    public static final Item[] bucket_item = {
            new BucketItem(still_fluid[0], new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
            new BucketItem(still_fluid[1], new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP)),
            new BucketItem(still_fluid[2], new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ITEM_GROUP))
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
    };

    public static void RegistItem(){
        for(int i = 0;i<items.length;i++){
            Registry.register(Registry.ITEM, new Identifier(MODID,itemnames[i]),items[i]);
        }
    }
}
