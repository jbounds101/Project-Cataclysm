package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;

public class DefuserItem extends Item {

    public DefuserItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        /*user.sendMessage(Text.literal("Used"));
        if (user.world.isClient()) {
            user.sendMessage(Text.literal(entity.toString()));

            if (entity.getClass().isAssignableFrom(TntEntity.class)) {
                // The entity is an explosive
                entity.remove(Entity.RemovalReason.KILLED);
                stack.damage(1, user,
                        (player) -> player.sendToolBreakStatus(hand));

                return ActionResult.SUCCESS;
            }

            return ActionResult.FAIL;
        }*/
        return ActionResult.PASS;
    }


}
