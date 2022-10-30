package net.bird.projectcataclysm.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class MasterWand extends Item {

    public Settings settings;
    public MasterWand(Settings settings) {
        super(settings);
        this.settings = settings;

    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {

        switch (SpellSwitcher.currentSpell) {
            case NONE:

                break;
            case LIFESWAP:
                WandLifeSwap wandLifeSwap = new WandLifeSwap(settings);
                wandLifeSwap.useOnEntity(stack, user, entity, hand);
                break;
            case SWITCH:
                WandItem wandItem = new WandItem(settings);
                wandItem.useOnEntity(stack, user, entity, hand);
                break;
            case WEBTRAP:

                break;
            case BLINK:

                break;
            case WATERTRAP:
                WaterTrapWand waterTrapWand = new WaterTrapWand(settings);
                waterTrapWand.useOnEntity(stack, user, entity, hand);
                break;
        }




        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);

    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        switch (SpellSwitcher.currentSpell) {
            case NONE:

                break;
            case WALL:
                WallWand wallWand = new WallWand(settings);
                wallWand.useOnBlock(context);
                break;
        }
        return ActionResult.PASS;
    }
}
