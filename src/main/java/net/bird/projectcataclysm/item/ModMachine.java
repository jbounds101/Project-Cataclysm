package net.bird.projectcataclysm.item;

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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ModMachine extends RangedWeaponItem implements Vanishable {
    public static final int field_30855 = 20;
    public static final int RANGE = 15;
    public static final int maxAmmo = 20;
    public static int currAmmo = 20;
    public ModMachine(Item.Settings settings) {
        super(settings);
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {

            ItemStack itemStack = playerEntity.getArrowType(stack);
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(Items.ARROW);
            }

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
            PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 3.0F, 1.0F);
            persistentProjectileEntity.setCritical(true);
            if (currAmmo > 0) {
                world.spawnEntity(persistentProjectileEntity);
                playerEntity.sendMessage(Text.literal("You have: " + currAmmo));
                //currAmmo--;
            } else if (currAmmo == 0) {
                String message = "Reloading";
                playerEntity.sendMessage(Text.literal("Reloading"));
                executor.schedule(ModMachine::reload, 2, TimeUnit.SECONDS);

            }






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
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        currAmmo--;
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
