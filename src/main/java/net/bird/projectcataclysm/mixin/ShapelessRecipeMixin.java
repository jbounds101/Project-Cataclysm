package net.bird.projectcataclysm.mixin;

import net.minecraft.recipe.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin {
    @Inject(method = "fits(II)Z", at = @At("RETURN"), cancellable = true)
    private void fitLarge(int width, int height, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && width * height <= 9);
    }
}
