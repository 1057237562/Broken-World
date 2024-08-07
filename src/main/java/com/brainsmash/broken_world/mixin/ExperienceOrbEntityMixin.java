package com.brainsmash.broken_world.mixin;

import com.brainsmash.broken_world.enchantment.SoulPullyEnchantment;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    @Shadow
    public PlayerEntity target;

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 64.0))
    public double applySoulPullyRangeBonusToTick64(double d) {
        double bonusRatio = this.target != null ? SoulPullyEnchantment.getExperienceOrbAttractionRadiusBonusRatio(this.target) : 0.0;
        return Math.pow(8 * (1.0 + bonusRatio), 2);
    }

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 8.0))
    public double applySoulPullyRangeBonusToTick8(double d) {
        double bonusRatio = this.target != null ? SoulPullyEnchantment.getExperienceOrbAttractionRadiusBonusRatio(this.target) : 0.0;
        return 8 * (1.0 + bonusRatio);
    }

    @ModifyConstant(method = "expensiveUpdate", constant = @Constant(doubleValue = 64.0))
    public double applySoulPullyRangeBonusToExpensiveUpdate64(double d) {
        double bonusRatio = this.target != null ? SoulPullyEnchantment.getExperienceOrbAttractionRadiusBonusRatio(this.target) : 0.0;
        return Math.pow(8 * (1.0 + bonusRatio), 2);
    }

    @ModifyConstant(method = "expensiveUpdate", constant = @Constant(doubleValue = 8.0))
    public double applySoulPullyRangeBonusToExpensiveUpdate8(double d) {
        double bonusRatio = this.target != null ? SoulPullyEnchantment.getExperienceOrbAttractionRadiusBonusRatio(this.target) : 0.0;
        return 24.0;
    }
}
