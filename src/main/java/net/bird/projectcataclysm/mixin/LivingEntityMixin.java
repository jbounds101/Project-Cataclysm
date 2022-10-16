package net.bird.projectcataclysm.mixin;

import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.item.custom.ScytheItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private static boolean shieldReduced;
    @Inject(method = "onKilledBy(Lnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
    private void scytheDrop(LivingEntity source, CallbackInfo ci) {
        if (!((LivingEntity)(Object)this).world.isClient && source != null) {
            if (source.getStackInHand(Hand.MAIN_HAND).getItem() instanceof ScytheItem) {
                if (((LivingEntity)(Object)this) instanceof WardenEntity) {
                    for (int i = 0; i < 3; i++) {
                        ItemEntity itemEntity = new ItemEntity(((LivingEntity) (Object) this).world, ((LivingEntity) (Object) this).getX(), ((LivingEntity) (Object) this).getY(), ((LivingEntity) (Object) this).getZ(), new ItemStack(Items.ECHO_SHARD));
                        ((LivingEntity) (Object) this).world.spawnEntity(itemEntity);
                    }
                }
                else {
                    ItemEntity itemEntity = new ItemEntity(((LivingEntity) (Object) this).world, ((LivingEntity) (Object) this).getX(), ((LivingEntity) (Object) this).getY(), ((LivingEntity) (Object) this).getZ(), new ItemStack(ModItems.SOUL_SHARD));
                    ((LivingEntity) (Object) this).world.spawnEntity(itemEntity);
                }
            }
        }
    }
    @ModifyVariable(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float reduceDamage(float amount) {
        if (amount > 30f && ((LivingEntity)(Object)this).getActiveItem().getItem() == ModItems.SILVER_SHIELD) {
            shieldReduced = true;
            return amount - 30f;
        }
        shieldReduced = false;
        return amount;
    }
    @Redirect(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;blockedByShield(Lnet/minecraft/entity/damage/DamageSource;)Z"))
    private boolean shieldCheck(LivingEntity instance, DamageSource source) {
        if (shieldReduced) {
            return false;
        }
        return instance.blockedByShield(source);
    }
}
