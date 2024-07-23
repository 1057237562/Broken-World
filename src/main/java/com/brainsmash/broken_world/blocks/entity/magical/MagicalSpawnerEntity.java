package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MagicalSpawnerEntity extends XpContainerEntity {

    public MagicalSpawnerEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.MAGICAL_SPAWNER_ENTITY_TYPE, pos, state);
    }

    private int spawnDelay = 20;
    private int minSpawnDelay = 20;
    private int maxSpawnDelay = 40;
    @Nullable
    private Entity renderedEntity;
    private int maxNearbyEntities = 10;
    private int requiredPlayerRange = 16;
    private int spawnRange = 4;
    public NbtCompound spawnEntity = new NbtCompound();

    private boolean isPlayerInRange(World world, BlockPos pos) {
        return world.isPlayerInRange((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5,
                this.requiredPlayerRange);
    }

    public void clientTick(World world, BlockPos pos) {
        if (isPlayerInRange(world, pos)) {
            Random random = world.getRandom();
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() + random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }
        }

    }

    public void serverTick(ServerWorld world, BlockPos pos) {
        if (this.isPlayerInRange(world, pos)) {
            if (this.spawnDelay == -1) {
                this.updateSpawns(world, pos);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {
                boolean bl = false;
                if (xpStorage.amount <= FluidConstants.BOTTLE / 2) {
                    return;
                }
                NbtCompound nbtCompound = spawnEntity;
                if (nbtCompound == null) {
                    return;
                }
                Optional<EntityType<?>> optional = EntityType.fromNbt(nbtCompound);
                if (optional.isEmpty()) {
                    this.updateSpawns(world, pos);
                    return;
                }

                NbtList nbtList = nbtCompound.getList("Pos", NbtElement.DOUBLE_TYPE);
                int j = nbtList.size();
                Random random = world.getRandom();
                double d = j >= 1 ? nbtList.getDouble(
                        0) : (double) pos.getX() + (random.nextDouble() - random.nextDouble()) * (double) this.spawnRange + 0.5;
                double e = j >= 2 ? nbtList.getDouble(1) : (double) (pos.getY() + random.nextInt(3) - 1);
                double f = j >= 3 ? nbtList.getDouble(
                        2) : (double) pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double) this.spawnRange + 0.5;
                if (world.isSpaceEmpty(optional.get().createSimpleBoundingBox(d, e, f))) {
                    BlockPos blockPos = new BlockPos(d, e, f);

                    Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, (entityx) -> {
                        entityx.refreshPositionAndAngles(d, e, f, entityx.getYaw(), entityx.getPitch());
                        return entityx;
                    });
                    if (entity == null) {
                        this.updateSpawns(world, pos);
                        return;
                    }

                    if (entity instanceof MobEntity mob) {
                        if (xpStorage.simulateExtract(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                                (long) (FluidConstants.BOTTLE * mob.getMaxHealth() / 16),
                                null) < (long) (FluidConstants.BOTTLE * mob.getMaxHealth() / 16)) {
                            return;
                        }
                        int k = world.getNonSpectatingEntities(entity.getClass(),
                                (new Box((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
                                        (double) (pos.getX() + 1), (double) (pos.getY() + 1),
                                        (double) (pos.getZ() + 1))).expand((double) this.spawnRange)).size();
                        if (k >= this.maxNearbyEntities) {
                            this.updateSpawns(world, pos);
                            return;
                        }

                        entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(),
                                random.nextFloat() * 360.0F, 0.0F);

                        if (!world.spawnNewEntityAndPassengers(entity)) {
                            this.updateSpawns(world, pos);
                            return;
                        }

                        world.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, pos, 0);
                        world.emitGameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
                        mob.playSpawnEffects();
                        try (Transaction transaction = Transaction.openOuter()) {
                            xpStorage.extract(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                                    (long) (FluidConstants.BOTTLE * mob.getMaxHealth() / 16), transaction);
                            transaction.commit();
                        }
                    }

                    bl = true;
                }

                if (bl) {
                    this.updateSpawns(world, pos);
                }

            }
        }
    }

    private void updateSpawns(World world, BlockPos pos) {
        Random random = world.random;
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("spawnEntity", spawnEntity);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        spawnEntity = (NbtCompound) nbt.get("spawnEntity");
    }
}
