package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.entity.impl.EntityDataExtension;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityDataExtension {
    @Shadow
    public World world;

    @Shadow
    protected abstract void setFlag(int index, boolean value);

    @Shadow
    protected abstract boolean getFlag(int index);

    private NbtElement element = new NbtCompound();


    @Inject(method = "readNbt", at = @At("TAIL"))
    public void readData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("bwdata")) {
            element = nbt.get("bwdata");
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    public void writeData(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        nbt.put("bwdata", element);
    }

    @Override
    public NbtElement getData() {
        return element;
    }

    @Override
    public void setData(NbtElement ele) {
        element = ele;
    }

}
