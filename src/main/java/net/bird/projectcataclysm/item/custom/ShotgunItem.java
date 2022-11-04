package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.entity.custom.SlugEntity;
import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ShotgunItem extends RangedWeaponItem implements Vanishable {
    public ShotgunItem(Settings settings) {
        super(settings);
    }
    public static final int maxAmmo = 5;

    public static int currAmmo = 5;

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack;
            itemStack = new ItemStack(ModItems.SLUG);

            if (!world.isClient) {

                SlugItem slugItem = (SlugItem) (itemStack.getItem() instanceof SlugItem ? itemStack.getItem() : ModItems.SLUG);
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

                PersistentProjectileEntity persistentProjectileEntity = slugItem.createSlug(world, itemStack, playerEntity);
                persistentProjectileEntity.setOwner(user);
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5F, 1.0F);
                persistentProjectileEntity.setCritical(true);
                double damage = persistentProjectileEntity.getDamage();
                persistentProjectileEntity.setDamage(damage * 3);
                persistentProjectileEntity.setPunch(3);

                if (currAmmo == 0) {
                    playerEntity.sendMessage(Text.literal("Reloading"));
                    executorService.schedule(PistolItem::reload, 2, TimeUnit.SECONDS);
                    playerEntity.addExhaustion(4);
                }

                if (currAmmo > 0) {
                    world.spawnEntity(persistentProjectileEntity);
                    ((PlayerEntity) user).getItemCooldownManager().set(this, 40);
                    currAmmo--;
                    playerEntity.sendMessage(Text.literal("You have:" + currAmmo));
                    //playerEntity.sendMessage(Text.literal("Damage: " + persistentProjectileEntity.getDamage()));
                    if (currAmmo == 0) {
                        ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
                        playerEntity.sendMessage(Text.literal("Reloading"));
                        executorService.schedule(ShotgunItem::reload, 1, TimeUnit.SECONDS);
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
