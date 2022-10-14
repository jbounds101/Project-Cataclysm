package net.bird.projectcataclysm.mixin;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.item.custom.ScytheItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    @Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean scytheBreakCheck(ItemStack stack) {
        if (((PlayerEntity)(Object)this).getStackInHand(Hand.MAIN_HAND).getItem() instanceof HoeItem) {
            return false;
        }
        return stack.isEmpty();
    }
    @Inject(method = "takeShieldHit(Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;disablesShield()Z"), cancellable = true)
    private void cancelDisable(CallbackInfo ci) {
        if (((PlayerEntity)(Object)this).getActiveItem().getItem() == ModItems.SILVER_SHIELD) {
            ci.cancel();
        }
    }
    @Redirect(method = "damageShield(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean shieldCheck(ItemStack instance, Item item) {
        return instance.isOf(item) || instance.isOf(ModItems.SILVER_SHIELD);
    }
    @Inject(method = "tick()V", at = @At("TAIL"))
    private void addShieldResistance(CallbackInfo ci) {
        if (((PlayerEntity)(Object)this).isHolding(ModItems.SILVER_SHIELD)) {
            ((PlayerEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, false, false, true));
        }
    }
}
