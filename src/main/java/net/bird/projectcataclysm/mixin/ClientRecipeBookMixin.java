package net.bird.projectcataclysm.mixin;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
    @Redirect(method = "getGroupForRecipe(Lnet/minecraft/recipe/Recipe;)Lnet/minecraft/client/recipebook/RecipeBookGroup;", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;getType()Lnet/minecraft/recipe/RecipeType;"))
    private static RecipeType<?> fabricatorCheck(Recipe<?> recipe) {
        if (recipe.getType() != ProjectCataclysmMod.FABRICATING) {
            return recipe.getType();
        }
        return RecipeType.CRAFTING;
    }
}
