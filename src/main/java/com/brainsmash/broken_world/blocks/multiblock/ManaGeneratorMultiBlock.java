package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.magical.InfusedCrystalBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ManaGeneratorMultiBlock extends Multiblock {
    public static Identifier ID = new Identifier(Main.MODID, "mana_gen");
    public BlockPos centre;

    public ManaGeneratorMultiBlock(World world, MatchResult match) {
        super(world, match);
        centre = match.bottomLeftPos().offset(Direction.UP, 1).offset(Direction.EAST, 1).offset(Direction.NORTH, 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().getBlockEntity(centre) instanceof InfusedCrystalBlockEntity entity) {
            entity.increaseMana(4);
        }
    }

    @Override
    public NbtCompound writeTag() {
        return super.writeTag();
    }

    @Override
    public void readTag(NbtCompound tag) {
        super.readTag(tag);
    }

    public static void register() {
        MultiblockLib.INSTANCE.registerMultiblock(ID, ManaGeneratorMultiBlock::new,
                MultiblockPatternKeyBuilder.start().where('C', CachedBlockPosition.matchesBlockState(
                        state -> state.getBlock() == Blocks.AMETHYST_CLUSTER)).where('B',
                        CachedBlockPosition.matchesBlockState(
                                state -> state.getBlock() == Blocks.AMETHYST_BLOCK)).where('I',
                        CachedBlockPosition.matchesBlockState(
                                state -> state.getBlock() == BlockRegister.get(BlockRegistry.INFUSED_CRYSTAL))).where(
                        ' ', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == Blocks.AIR)).build());
    }
}
