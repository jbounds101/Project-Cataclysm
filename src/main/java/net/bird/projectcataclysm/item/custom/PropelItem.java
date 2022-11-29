package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class PropelItem extends RangedWeaponItem implements Vanishable {
    public PropelItem(Settings settings) {
        super(settings);
    }
    public static final int maxAmmo = 10;
    public static int currAmmo = 10;

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            if (!world.isClient) {
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

                if (currAmmo > 0) {
                    float vec3f = playerEntity.getYaw();
                    float pitch = playerEntity.getPitch();
                    Vec3d vec3d = playerEntity.getVelocity();
                    float xValue = 0;
                    double yValue = 0;
                    float dir = playerEntity.getHorizontalFacing().asRotation();

                    if (pitch <= 11.25) {
                        yValue = 0;
                    }
                    else if (pitch > 11.25 && pitch <= 22.5) {
                        yValue = 0.75;
                    }
                    else if (pitch > 22.5 && pitch <= 45) {
                        yValue = .95;
                    }
                    else if (pitch > 45 && pitch <= 67.5) {
                        yValue = 1;
                    }
                    else if (pitch > 67.5 && pitch <= 80)  {
                        yValue = 1.25;
                    }
                    else {
                        yValue = 1.5;
                        dir = -1;
                    }

                    if (dir == 0) {
                        //facing positive Z
                        if (vec3f <= 22.5 && vec3f >= -22.5) {
                            xValue = 0;
                        } else {
                            xValue = vec3f;
                        }
                        user.addVelocity(user.getX() - (user.getX() - xValue), yValue, user.getZ() - (user.getZ() + 3));
                        user.velocityModified = true;
                    }
                    else if (dir == 180) {
                        //facing negative Z
                        if ((vec3f >= 157.5 && vec3f <= 180) || (vec3f <= -157.5 && vec3f >= -180)) {
                            xValue = 0;
                        } else {
                            xValue = vec3f;
                        }
                        user.addVelocity(user.getX() - (user.getX() - xValue), yValue, user.getZ() - (user.getZ() - 3));
                        user.velocityModified = true;
                    }
                    else if (dir == 90) {
                        //facing negative X
                        if ((vec3f >= 67.5 && vec3f <= 112.5)) {
                            xValue = 0;
                        }
                        else if (vec3f > 112.5 && vec3f <= 135) {
                            xValue = -vec3f;
                        }
                        else {
                            xValue = vec3f;
                        }
                        user.addVelocity(user.getX() - (user.getX() - 3), yValue, user.getZ() - (user.getZ() + xValue));
                        user.velocityModified = true;
                    }
                    else if (dir == 270) {
                        //facing positive X
                        if ((vec3f <= -67.5 && vec3f >= -112.5)) {
                            xValue = 0;
                        }
                        else if (vec3f < -112.5 && vec3f >= -135) {
                            xValue = -vec3f;
                        }
                        else {
                            xValue = vec3f;
                        }
                        user.addVelocity(user.getX() - (user.getX() + 3), yValue, user.getZ() - (user.getZ() - xValue));
                        user.velocityModified = true;
                    }
                    else if (dir == -1) {
                        user.addVelocity(0, yValue, 0);
                        user.velocityModified = true;
                    }

                    ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
                    currAmmo--;
                    playerEntity.sendMessage(Text.literal("You have: " + currAmmo));

                    //playerEntity.sendMessage(Text.literal("Damage: " + persistentProjectileEntity.getDamage()));
                    if (currAmmo == 0) {
                        ((PlayerEntity) user).getItemCooldownManager().set(this, 40);
                        playerEntity.sendMessage(Text.literal("Reloading"));
                        executorService.schedule(PropelItem::reload, 2, TimeUnit.SECONDS);
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
