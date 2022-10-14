package net.bird.projectcataclysm.item.custom;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.Random;

public class WandLifeSwap extends Item {

    public WandLifeSwap(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        Random random = new Random();
        float randNum = 1 + random.nextFloat(4);
        entity.damage(DamageSource.GENERIC, randNum * 2);
        user.heal(randNum * 2);
        user.getItemCooldownManager().set(this, 40);
        return super.useOnEntity(stack, user, entity, hand);
    }


}

