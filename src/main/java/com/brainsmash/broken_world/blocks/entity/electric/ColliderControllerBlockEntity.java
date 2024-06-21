package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.electric.ColliderCoilBlock;
import com.brainsmash.broken_world.blocks.electric.ColliderControllerBlock;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.ColliderRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.ColliderControllerGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import com.brainsmash.broken_world.util.PosHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColliderControllerBlockEntity extends ConsumerBlockEntity implements ImplementedInventory, SidedInventory, NamedScreenHandlerFactory {

    protected List<ColliderCoilBlockEntity> coilBlockEntityList = null;
    protected BlockPos colliderWSCornerPos = null;
    protected final RecipeManager.MatchGetter<ImplementedInventory, ? extends ColliderRecipe> matchGetter;
    protected final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    protected static final int INPUT_A = 0;
    protected static final int INPUT_B = 1;
    protected static final int OUTPUT = 2;
    protected static final int POWER = 3;
    public static final int[] TOP_SLOTS = new int[]{INPUT_A};
    public static final int[] SIDE_SLOTS = new int[]{INPUT_B};
    public static final int[] BOTTOM_SLOTS = new int[]{OUTPUT};
    public static final int COLLIDER_SIDE_LENGTH = 15;
    public static final int PROPERTY_COUNT = 4 + 4 * COLLIDER_SIDE_LENGTH - 4;
    protected boolean firstTick = true;
    protected ColliderRecipe matchedRecipe = null;
    protected PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return getEnergy();
                case 1:
                    return getMaxCapacity();
                case 2:
                    return progression;
                case 3:
                    return maxProgression;
                default:
                    if (index < 4 || index >= 4 + 4 * COLLIDER_SIDE_LENGTH - 4) return -1;
                    if (coilBlockEntityList == null) return 0xFF_5A5A5A;
                    return getCoilStatusColor(coilBlockEntityList.get(index - 4));
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> setEnergy(value);
                case 1 -> setMaxCapacity(value);
                case 2 -> progression = value;
                case 3 -> maxProgression = value;
            }
        }

        @Override
        public int size() {
            return PROPERTY_COUNT;
        }
    };

    public ColliderControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.COLLIDER_CONTROLLER_ENTITY_TYPE, pos, state);
        setMaxCapacity(200);
        progression = 0;
        maxProgression = 10;
        powerConsumption = 4;
        matchGetter = RecipeManager.createCachedMatchGetter(ColliderRecipe.Type.INSTANCE);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        int[] posArray = nbt.getIntArray("ws_corner");
        if (posArray.length != 3) {
            coilBlockEntityList = null;
            colliderWSCornerPos = null;
        } else {
            colliderWSCornerPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
            firstTick = true;
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (colliderWSCornerPos != null) {
            int[] posArray = new int[]{
                    colliderWSCornerPos.getX(),
                    colliderWSCornerPos.getY(),
                    colliderWSCornerPos.getZ()
            };
            nbt.putIntArray("ws_corner", posArray);
        }
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient()) {
            if (firstTick) {
                firstTick = false;
                if (colliderWSCornerPos != null) collectCoilEntities();
                matchedRecipe = matchGetter.getFirstMatch(this, world).orElse(null);
            }
            if (coilBlockEntityList != null && matchedRecipe != null) {
                startCoils();
                running = checkEnergy() && areCoilsCharged();
            } else {
                running = false;
                stopCoils();
            }
            if (running) {
                if (progression == maxProgression) {
                    dischargeCoils();
                    progression = 0;
                    shrinkInputStacks(matchedRecipe);
                    outputProduct(matchedRecipe);
                    matchedRecipe = matchGetter.getFirstMatch(this, world).orElse(null);
                } else {
                    progression++;
                }
            }
        }
        super.tick(world, pos, state, blockEntity);
    }

    protected void shrinkInputStacks(ColliderRecipe recipe) {
        ItemStack stack1 = getStack(INPUT_A);
        ItemStack stack2 = getStack(INPUT_B);
        if (recipe.a().test(stack1) && stack1.getCount() >= recipe.amountA() && recipe.b().test(
                stack2) && stack2.getCount() >= recipe.amountB()) {
            stack1.decrement(recipe.amountA());
            stack2.decrement(recipe.amountB());
        } else {
            stack2.decrement(recipe.amountA());
            stack1.decrement(recipe.amountB());
        }
    }

    protected void outputProduct(ColliderRecipe recipe) {
        ItemStack outputSlot = getStack(OUTPUT);
        ItemStack productStack = recipe.craft(this);
        int insertAmount;
        if (outputSlot.isEmpty()) {
            insertAmount = Math.min(productStack.getMaxCount(), productStack.getCount());
            setStack(OUTPUT, new ItemStack(productStack.getItem(), insertAmount));
        } else if (outputSlot.isOf(productStack.getItem())) {
            insertAmount = Math.min(outputSlot.getMaxCount() - outputSlot.getCount(), productStack.getCount());
            outputSlot.increment(insertAmount);
        } else {
            insertAmount = 0;
        }
        EntityHelper.spawnItem(world, new ItemStack(productStack.getItem(), productStack.getCount() - insertAmount), 1,
                Direction.UP, pos);
    }

    protected void dischargeCoils() {
        if (coilBlockEntityList == null) return;
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.discharge();
        }
    }

    protected void startCoils() {
        if (coilBlockEntityList == null) return;
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.start();
        }
    }

    protected void stopCoils() {
        if (coilBlockEntityList == null) return;
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.stop();
        }
    }

    protected boolean areCoilsCharged() {
        if (coilBlockEntityList == null) return false;
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            if (!coil.isCharged()) return false;
        }
        return true;
    }

    public void onMultiblockBreak() {
        stopCoils();
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.unbindController();
        }
        coilBlockEntityList = null;
        colliderWSCornerPos = null;
        progression = 0;
        markDirty();
    }

    public boolean hasBoundCoils() {
        return coilBlockEntityList != null;
    }

    protected void tryAssembleMultiblock() {
        if (world == null || world.isClient()) return;

        Direction d = null;
        boolean bl = false;
        Block desiredCoilType = null;
        for (Direction d1 : Direction.values()) {
            if (world.getBlockState(pos.offset(d1)).getBlock() instanceof ColliderCoilBlock block) {
                if (d == null) {
                    d = d1;
                    desiredCoilType = block;
                } else {
                    if (d.getAxis().isVertical() || d.getAxis() == d1.getAxis() || bl || !block.getDefaultState().isOf(
                            desiredCoilType)) return;
                    bl = true;
                }
            }
        }
        if (d == null) return;

        BlockPos.Mutable p = pos.offset(d).mutableCopy();
        Direction[] ws = {
                Direction.WEST,
                Direction.SOUTH
        };
        int m = world.getBlockState(p.offset(ws[0])).isOf(desiredCoilType) ? 0 : 1;
        int cnt = 0;
        for (int i = 0; i < 2; i++) {
            for (; cnt < COLLIDER_SIDE_LENGTH * 2 - 2; cnt++) {
                if (world.getBlockState(p.offset(ws[m])).isOf(desiredCoilType)) p.move(ws[m]);
                else break;
            }
            m = (m + 1) % 2;
        }

        coilBlockEntityList = new ArrayList<>();
        colliderWSCornerPos = new BlockPos(p);
        Runnable cleanup = () -> {
            coilBlockEntityList = null;
            colliderWSCornerPos = null;
        };
        Direction d1 = Direction.NORTH;
        for (int i = 0; i < 4; i++) {
            for (Direction d2 : Direction.values()) {
                if (d2 == d1 || d2 == d1.rotateClockwise(Direction.Axis.Y)) continue;
                Block block = world.getBlockState(p.offset(d2)).getBlock();
                if (block instanceof ColliderCoilBlock || block instanceof ColliderControllerBlock && !p.offset(
                        d2).equals(pos)) {
                    cleanup.run();
                    return;
                }
            }
            if (world.getBlockState(p).isOf(desiredCoilType) && world.getBlockEntity(
                    p) instanceof ColliderCoilBlockEntity entity) {
                coilBlockEntityList.add(entity);
            } else {
                cleanup.run();
                return;
            }

            for (int j = 0; j < COLLIDER_SIDE_LENGTH - 2; j++) {
                if (!world.getBlockState(p.offset(d1)).isOf(desiredCoilType)) {
                    cleanup.run();
                    return;
                }
                p.move(d1);
                for (Direction d2 : Direction.values()) {
                    if (d2.getAxis() == d1.getAxis()) continue;
                    Block block = world.getBlockState(p.offset(d2)).getBlock();
                    if (block instanceof ColliderCoilBlock || block instanceof ColliderControllerBlock && !p.offset(
                            d2).equals(pos)) {
                        cleanup.run();
                        return;
                    }
                }
                if (world.getBlockEntity(p) instanceof ColliderCoilBlockEntity entity1) {
                    coilBlockEntityList.add(entity1);
                } else {
                    cleanup.run();
                    return;
                }
            }
            p.move(d1);
            d1 = d1.rotateClockwise(Direction.Axis.Y);
        }

        sortCoilEntityList();
        for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
            coil.bindController(this);
        }
        markDirty();
    }

    protected void collectCoilEntities() {
        if (world == null) {
            Main.LOGGER.error("Could not collect coil block entities, because the controller's world is null!");
            return;
        }
        coilBlockEntityList = new ArrayList<>();
        int r = COLLIDER_SIDE_LENGTH / 2;
        PosHelper.squareAround(colliderWSCornerPos.add(r, 0, -r), r).forEach(p -> {
            if (world.getBlockEntity(p) instanceof ColliderCoilBlockEntity coil) {
                coilBlockEntityList.add(coil);
            } else {
                Main.LOGGER.error(
                        "Could not collect coil block entity at {}, because this entity is not instance of ColliderCoilBlockEntity!",
                        p);
            }
        });
        if (coilBlockEntityList.size() != COLLIDER_SIDE_LENGTH * 4 - 4) {
            Main.LOGGER.error("Failed to collect coil block entities! This collider will not work!");
            coilBlockEntityList = null;
            colliderWSCornerPos = null;
        } else {
            sortCoilEntityList();
            for (ColliderCoilBlockEntity coil : coilBlockEntityList) {
                coil.bindController(this);
            }
        }
    }

    // So that the list fits into the render order of WColliderStatus
    protected void sortCoilEntityList() {
        int d = COLLIDER_SIDE_LENGTH;
        coilBlockEntityList.sort((a, b) -> {
            BlockPos posA = a.getPos().subtract(colliderWSCornerPos.add(d / 2, 0, -d / 2));
            BlockPos posB = b.getPos().subtract(colliderWSCornerPos.add(d / 2, 0, -d / 2));
            float thetaA = PosHelper.cartesianToPolar(new Vec2f(posA.getX(), posA.getZ())).y;
            float thetaB = PosHelper.cartesianToPolar(new Vec2f(posB.getX(), posB.getZ())).y;
            thetaA = 2 * MathHelper.PI - thetaA;
            thetaB = 2 * MathHelper.PI - thetaB;
            thetaA = (1.25F * MathHelper.PI - thetaA + 2 * MathHelper.PI) % (2 * MathHelper.PI);
            thetaB = (1.25F * MathHelper.PI - thetaB + 2 * MathHelper.PI) % (2 * MathHelper.PI);
            return Float.compare(thetaA, thetaB);
        });
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    protected int getCoilStatusColor(ColliderCoilBlockEntity coil) {
        if (progression > 0) return 0xFF_779EFF; // Light blue if collision in progress
        if (coil.isCharged()) return 0xFF_0051EE; // Blue if coil is fully charged
        if (!coil.checkEnergy()) return 0xFF_EE4200; // Deep orange if coil has low energy
        return 0xFF_FFFF00 - ((int) (0xFF * coil.getChargePercentage()) << 16); // Yellow to green as the coil gets charged
    }

    public void onUse() {
        if (coilBlockEntityList != null) return;
        tryAssembleMultiblock();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) return TOP_SLOTS;
        if (side.getAxis().isHorizontal()) return SIDE_SLOTS;
        return BOTTOM_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return switch (slot) {
            case 0 -> dir == Direction.UP;
            case 1 -> dir != null && dir.getAxis().isHorizontal();
            default -> false;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 2;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        Main.LOGGER.info("Setting stack {} to {}", slot, stack);
        ImplementedInventory.super.setStack(slot, stack);
        if (slot == INPUT_A || slot == INPUT_B) {
            ColliderRecipe recipeMatch = matchGetter.getFirstMatch(this, world).orElse(null);
            Identifier id = recipeMatch == null ? null : recipeMatch.getId();
            if (!Objects.equals(id, matchedRecipe == null ? null : matchedRecipe.getId())) {
                this.matchedRecipe = recipeMatch;
                progression = 0;
                markDirty();
            }
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ColliderControllerGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
}