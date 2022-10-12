package net.bird.projectcataclysm.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SoulGemItem extends Item {
    public SoulGemItem(Item.Settings settings) {
        super(settings);
    }
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}