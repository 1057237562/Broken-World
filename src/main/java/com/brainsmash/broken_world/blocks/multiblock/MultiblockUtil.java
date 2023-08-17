package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.blocks.multiblock.util.MultiblockProvider;
import com.brainsmash.broken_world.util.SerializationHelper;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.brainsmash.broken_world.Main.MODID;

public class MultiblockUtil {
    public static Map<Identifier, MultiblockPattern> patternMap = Maps.newHashMap();
    public static Map<Identifier, MultiblockProvider> providerMap = Maps.newHashMap();
    public static Logger LOGGER = LoggerFactory.getLogger(MultiblockUtil.class);

    public static void revertToBlock(World world, BlockPos pos, Vec3i sz) {
        for (int x = 0; x < sz.getX(); x++) {
            for (int y = 0; y < sz.getY(); y++) {
                for (int z = 0; z < sz.getZ(); z++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!(world.getBlockEntity(p) instanceof DummyBlockEntity)) continue;
                    BlockState originalBlock = ((DummyBlockEntity) world.getBlockEntity(p)).getImitateBlockState();
                    if (originalBlock.getBlock() == Blocks.AIR) continue;
                    NbtCompound nbt = ((DummyBlockEntity) world.getBlockEntity(p)).getImitateNbt();
                    world.setBlockState(p, originalBlock);
                    if (originalBlock.getBlock() instanceof BlockWithEntity bwe) {
                        world.getBlockEntity(p).readNbt(nbt);
                    }
                }
            }
        }
    }

    public static void tryAssemble(World world, Identifier identifier, BlockPos pos, BlockPos anchor) {
        MultiblockPattern pattern = patternMap.get(identifier);
        if (pattern == null) {
            LOGGER.warn("MultiblockPattern {} does not exist", identifier);
            return;
        }
        for (BlockRotation rotation : BlockRotation.values()) {
            if (pattern.test(world, pos, rotation, anchor)) {
                Vec3i sz = new BlockPos(pattern.size).rotate(rotation);
                convertToDummy(world, pos.subtract(anchor.rotate(rotation)), sz, anchor, identifier);
                return;
            }
        }
    }

    public static void registerMultiblock(Identifier identifier, MultiblockProvider provider) {
        providerMap.put(identifier, provider);
    }

    private static void convertToDummy(World world, BlockPos pos, Vec3i sz, BlockPos anchor, @Nullable Identifier identifier) {
        MultiblockProvider provider = providerMap.get(identifier);
        for (int x = 0; x < sz.getX(); x++) {
            for (int y = sz.getY() - 1; y >= 0; y--) {
                for (int z = 0; z < sz.getZ(); z++) {
                    BlockPos p = pos.add(x, y, z);
                    NbtCompound originalBlock = SerializationHelper.saveBlockState(world.getBlockState(p));
                    NbtCompound nbt = new NbtCompound();
                    BlockEntity originalBlockEntity;
                    if (world.getBlockState(p).getBlock() instanceof BlockWithEntity bwe) {
                        originalBlockEntity = world.getBlockEntity(p);
                        if (originalBlockEntity instanceof DummyBlockEntity dummyBlockEntity) {
                            dummyBlockEntity.setLink(pos);
                            continue;
                        }
                        nbt = originalBlockEntity.createNbt();
                        world.removeBlockEntity(p);
                    }
                    if (world.getBlockState(p).getBlock() == Blocks.AIR) continue;
                    if (anchor.equals(new BlockPos(x, y, z))) {
                        world.setBlockState(p, multiblock.getDefaultState());
                        if (world.getBlockEntity(p) instanceof MultiblockEntity mbe) {
                            mbe.setMultiblockSize(sz); // Master
                            mbe.setAnchor(pos);
                            mbe.setType(identifier);
                        }
                    } else world.setBlockState(p, dummy.getDefaultState());
                    DummyBlockEntity dummy = (DummyBlockEntity) world.getBlockEntity(p);
                    dummy.setImitateBlock(SerializationHelper.loadBlockState(originalBlock), nbt);
                    dummy.setLink(pos.add(anchor)); // Slave
                    dummy.visible = !(provider != null && provider.get(world, pos.add(anchor)).hasCustomModel());
                }
            }
        }
    }

    public static BlockEntityType<DummyBlockEntity> DUMMY_ENTITY_TYPE;
    public static BlockEntityType<MultiblockEntity> MULTIBLOCK_ENTITY_TYPE;
    public static Block dummy;
    public static Block multiblock;

    public static void registMultiblock() {
        dummy = Registry.register(Registry.BLOCK, new Identifier(MODID, "dummy"),
                new DummyBlock(FabricBlockSettings.of(Material.BARRIER).strength(1.0F, 6.0F).nonOpaque()));
        DUMMY_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "dummy"),
                FabricBlockEntityTypeBuilder.create(DummyBlockEntity::new, dummy).build());

        multiblock = Registry.register(Registry.BLOCK, new Identifier(MODID, "multiblock"),
                new Multiblock(FabricBlockSettings.of(Material.BARRIER).strength(1.0F, 6.0F).nonOpaque()));
        MULTIBLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "multiblock"),
                FabricBlockEntityTypeBuilder.create(MultiblockEntity::new, multiblock).build());

        BlockRenderLayerMap.INSTANCE.putBlock(dummy, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(multiblock, RenderLayer.getTranslucent());
    }

    public static void registMultiblockClientSide() {
        BlockEntityRendererRegistry.register(DUMMY_ENTITY_TYPE, DummyBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(MULTIBLOCK_ENTITY_TYPE, MultiblockEntityRenderer::new);
    }

}
