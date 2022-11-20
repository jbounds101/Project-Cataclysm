package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class BazookaItem extends RangedWeaponItem {

    public static final int maxAmmo = 1;
    public static int currAmmo = 1;

    public BazookaItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack;
            itemStack = new ItemStack(ModItems.RPG);
            if (!world.isClient) {
                RPGItem rpgItem = (RPGItem) (itemStack.getItem() instanceof RPGItem ? itemStack.getItem() : ModItems.RPG);
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

                PersistentProjectileEntity persistentProjectileEntity = rpgItem.createRPG(world, itemStack, playerEntity);
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 8.0F, 1.0F);
                persistentProjectileEntity.setCritical(true);
                double damage = persistentProjectileEntity.getDamage();
                persistentProjectileEntity.setDamage(damage * 1);

                if (currAmmo == 0) {
                    ((PlayerEntity) user).getItemCooldownManager().set(this, 40);
                    playerEntity.sendMessage(Text.literal("Reloading"));
                    executorService.schedule(BazookaItem::reload, 2, TimeUnit.SECONDS);
                    playerEntity.addExhaustion(4);
                }

                if (currAmmo > 0) {
                    world.spawnEntity(persistentProjectileEntity);
                    ((PlayerEntity) user).getItemCooldownManager().set(this, 5);
                    currAmmo--;
                    playerEntity.sendMessage(Text.literal("You have:" + currAmmo));

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

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    @Override
    public int getRange() {
        return 0;
    }
}
