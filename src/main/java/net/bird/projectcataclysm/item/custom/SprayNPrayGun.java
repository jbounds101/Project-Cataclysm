package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.item.ModMachine;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class SprayNPrayGun extends RangedWeaponItem {

    public SprayNPrayGun(Settings settings) {
        super(settings);
    }

    public static final int maxAmmo = 60;
    public static int currAmmo = 60;

    public static int x = 0;

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        x++;
        if (x % 5 == 0) {
            if (user instanceof PlayerEntity playerEntity) {

                ItemStack itemStack;
                itemStack = new ItemStack(ModItems.BULLET);


                if (!world.isClient) {

                    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    BulletItem bulletItem = (BulletItem)(itemStack.getItem() instanceof BulletItem ? itemStack.getItem() : ModItems.BULLET);

                    /* BULLET 1 */
                    PersistentProjectileEntity persistentProjectileEntity = bulletItem.createBullet(world, itemStack, playerEntity);

                    Random randDiv = new Random();

                    Random randRoll = new Random();

                    Random randSpeed = new Random();

                    float randD = 1 + randDiv.nextFloat(50);
                    float randR = randRoll.nextFloat(2);
                    float randS = 3 + randSpeed.nextFloat(10);
                    persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                            randR, randS, randD);

                    persistentProjectileEntity.setCritical(true);
                    double damage = persistentProjectileEntity.getDamage();
                    persistentProjectileEntity.setDamage(damage * 1.25);

                    /* BULLET 2 */
                    PersistentProjectileEntity persistentProjectileEntity2 = bulletItem.createBullet(world, itemStack, playerEntity);
                    Random randDiv2 = new Random();

                    Random randRoll2 = new Random();

                    Random randSpeed2 = new Random();


                    float randD2 = 1 + randDiv2.nextFloat(50);
                    float randR2 = randRoll2.nextFloat(2);
                    float randS2 = 3 + randSpeed2.nextFloat(10);
                    persistentProjectileEntity2.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                            randR2, randS2, randD2);
                    persistentProjectileEntity2.setCritical(true);
                    double damage2 = persistentProjectileEntity2.getDamage();
                    persistentProjectileEntity2.setDamage(damage2 * 1.5);


                    if (currAmmo > 0) {
                        world.spawnEntity(persistentProjectileEntity);
                        world.spawnEntity(persistentProjectileEntity2);
                        currAmmo -= 2;
                        playerEntity.sendMessage(Text.literal("You have: " + currAmmo + " ammo left"));

                        if (currAmmo == 0) {
                            ((PlayerEntity) user).getItemCooldownManager().set(this, 40);
                            ((PlayerEntity) user).addExhaustion(12);
                            playerEntity.sendMessage(Text.translatable("Reloading"), false);
                            executor.schedule(SprayNPrayGun::reload, 2, TimeUnit.SECONDS);

                        }
                    }
                }
            }
        }

    }

    public static void reload() {
        currAmmo = maxAmmo;
    }

    public int getMaxUseTime(ItemStack stack) {

        return 900000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

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
        return 0;
    }
}
