package com.brainsmash.broken_world.items.electrical;

import com.brainsmash.broken_world.Main;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MiningDrillItem extends ElectricMiningToolItem implements BatteryHolder {
    public static final TagKey<Block> MINING_DRILL_MINEABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier(Main.MODID, "mineable/mining_drill"));
    public MiningDrillItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, material, MINING_DRILL_MINEABLE, settings);
    }
}
