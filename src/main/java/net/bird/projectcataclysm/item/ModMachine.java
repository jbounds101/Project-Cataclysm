package net.bird.projectcataclysm.item;

import net.bird.projectcataclysm.item.custom.BulletItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ModMachine extends RangedWeaponItem implements Vanishable {
    public static final int field_30855 = 20;
    public static final int RANGE = 15;
    public static final int maxAmmo = 50;
    public static int currAmmo = 50;
    public ModMachine(Item.Settings settings) {
        super(settings);
    }

    public static int x = 0;
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        x++;
        if (x % 5 == 0) {
            if (user instanceof PlayerEntity playerEntity) {

                ItemStack itemStack = playerEntity.getArrowType(stack);
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(ModItems.BULLET);
                }

                if (!world.isClient) {

                    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    //ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                    BulletItem bulletItem = (BulletItem)(itemStack.getItem() instanceof BulletItem ? itemStack.getItem() : ModItems.BULLET);
                    PersistentProjectileEntity persistentProjectileEntity = bulletItem.createBullet(world, itemStack, playerEntity);
                    persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 8F, 1.0F);
                    persistentProjectileEntity.setCritical(true);
                    double damage = persistentProjectileEntity.getDamage();
                    persistentProjectileEntity.setDamage(damage * 0.25);
                    if (currAmmo > 0) {
                        world.spawnEntity(persistentProjectileEntity);
                        currAmmo--;
                        playerEntity.sendMessage(Text.translatable("You have: " + currAmmo), false);

                        if (currAmmo == 0) {
                            ((PlayerEntity) user).getItemCooldownManager().set(this, 40);
                            ((PlayerEntity) user).addExhaustion(12);
                            playerEntity.sendMessage(Text.translatable("Reloading"), false);
                            executor.schedule(ModMachine::reload, 2, TimeUnit.SECONDS);

                        }
                    }
                }
            }
        }

    }
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {


    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Ammo in Clip: " + currAmmo).formatted(Formatting.BLUE));
        }
        else {
            tooltip.add(Text.literal("Press Shift for Ammo Count").formatted(Formatting.YELLOW));
        }
    }
    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 3.0F;
        f = (f * f + f * 10.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
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

    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    public int getRange() {
        return 15;
    }
}
