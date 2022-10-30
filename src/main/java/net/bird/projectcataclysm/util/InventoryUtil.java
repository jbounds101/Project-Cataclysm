package net.bird.projectcataclysm.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil {
    public static boolean hasPlayerStackInInventory(PlayerEntity player, Item item) {

         if (item == null) {
             return false;
         }
        for(int i = 0; i < player.getInventory().size(); i++) {
            ItemStack currentStack = player.getInventory().getStack(i);
            if (!currentStack.isEmpty() && currentStack.isItemEqual(new ItemStack(item))) {
                return true;
            }
        }
        return false;
    }
}
