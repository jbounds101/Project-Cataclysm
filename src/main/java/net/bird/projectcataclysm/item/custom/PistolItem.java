package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class PistolItem extends RangedWeaponItem implements Vanishable {
    public static final int maxAmmo = 10;
    public static int currAmmo = 10;

    public PistolItem(Settings settings) {
        super(settings);
    }



    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            //ItemStack itemStack = playerEntity.getArrowType(stack);
            ItemStack itemStack;
            itemStack = new ItemStack(ModItems.BULLET);
            /*if (itemStack.isEmpty()) {
                itemStack = new ItemStack(Items.ARROW);
            }*/
            if (!world.isClient) {
                BulletItem bulletItem = (BulletItem) (itemStack.getItem() instanceof BulletItem ? itemStack.getItem() : ModItems.BULLET);
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                //ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                //PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                PersistentProjectileEntity persistentProjectileEntity = bulletItem.createBullet(world, itemStack, playerEntity);
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 3.0F, 1.0F);
                persistentProjectileEntity.setCritical(true);
                double damage = persistentProjectileEntity.getDamage();
                persistentProjectileEntity.setDamage(damage * 1);

                if (currAmmo == 0) {
                    playerEntity.sendMessage(Text.literal("Reloading"));
                    executorService.schedule(PistolItem::reload, 2, TimeUnit.SECONDS);
                    playerEntity.addExhaustion(4);
                }

                if (currAmmo > 0) {
                    world.spawnEntity(persistentProjectileEntity);
                    ((PlayerEntity) user).getItemCooldownManager().set(this, 5);
                    currAmmo--;
                    playerEntity.sendMessage(Text.literal("You have:" + currAmmo));
                    //playerEntity.sendMessage(Text.literal("Damage: " + persistentProjectileEntity.getDamage()));
                    if (currAmmo == 0) {
                        ((PlayerEntity) user).getItemCooldownManager().set(this, 40);
                        playerEntity.sendMessage(Text.literal("Reloading"));
                        executorService.schedule(PistolItem::reload, 2, TimeUnit.SECONDS);
                        playerEntity.addExhaustion(4);
                    }
                }
            }
        }
    }

    public static void reload() {
        currAmmo = maxAmmo;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Ammo in Clip: " + currAmmo).formatted(Formatting.BLUE));
        }
        else {
            tooltip.add(Text.literal("Press Shift for Ammo Count").formatted(Formatting.YELLOW));
        }
    }

    public static final TagKey<Item> BULLET = TagKey.of(Registry.ITEM_KEY, new Identifier("bullet"));

    public static final Predicate<ItemStack> BULLETS = (stack) -> stack.isIn(BULLET);
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BULLETS;
    }

    @Override
    public int getRange() {
        return 15;
    }
}
