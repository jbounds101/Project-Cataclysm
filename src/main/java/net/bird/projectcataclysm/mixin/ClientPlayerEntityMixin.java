package net.bird.projectcataclysm.mixin;

import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean shieldCheck(ClientPlayerEntity instance) {
        if (instance.getActiveItem().getItem() == ModItems.SILVER_SHIELD) {
            return false;
        }
        return instance.isUsingItem();
    }
}
