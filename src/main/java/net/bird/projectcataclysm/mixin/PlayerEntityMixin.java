package net.bird.projectcataclysm.mixin;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.item.custom.ScytheItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Unique private static boolean isStrong;
    @Unique private static float damageDealt;
    @ModifyVariable(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"), ordinal = 0)
    private boolean setIsStrong(boolean bl) {
        isStrong = bl;
        return bl;
    }
    @ModifyVariable(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"), ordinal = 3)
    private boolean scytheCheck(boolean bl4) {
        if (((PlayerEntity)(Object)this).getStackInHand(Hand.MAIN_HAND).getItem() instanceof ScytheItem && isStrong) {
            return true;
        }
        return bl4;
    }
    @ModifyVariable(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 0), ordinal = 0)
    private float setDamageDealt(float f) {
        damageDealt = f;
        return f;
    }
    @ModifyVariable(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"), ordinal = 4)
    private float sweepDamage(float l) {
        if (((PlayerEntity)(Object)this).getStackInHand(Hand.MAIN_HAND).getItem() instanceof ScytheItem) {
            return damageDealt;
        }
        return l;
    }
}
