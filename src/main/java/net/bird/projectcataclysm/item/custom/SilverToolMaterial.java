package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class SilverToolMaterial implements ToolMaterial {
    public static final SilverToolMaterial INSTANCE = new SilverToolMaterial();
    @Override
    public int getDurability() {
        return 59;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 1.0F;
    }

    @Override
    public float getAttackDamage() {
        return 4.5F;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 22;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ModItems.SILVER_INGOT);
    }
}
