package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.function.Predicate;

public class PortalGunItem extends RangedWeaponItem {

    static Block blockHitBlue;


    static BlockPos blockHitBluePos;

    static Block blockHitOrange;
    static BlockPos blockHitOrangePos;

    static boolean bluePortalExists;
    static boolean orangePortalExists;

    public static final int BLUE = 1;
    public static final int ORANGE = 2;

    static int currentPortal = BLUE;

    static BlockPos bluePortalPos;
    static BlockPos orangePortalPos;

    public static int timesFired = 0;

    static World world;

    static LivingEntity user;

    public PortalGunItem(Settings settings) {
        super(settings);
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        PortalGunItem.world = world;
        PortalGunItem.user = user;

        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack;
            itemStack = new ItemStack(ModItems.PORTAL_PROJECTILE);
            if (!world.isClient) {
                PortalProjectile portalProjectile = (PortalProjectile) (itemStack.getItem() instanceof PortalProjectile ? itemStack.getItem() : ModItems.PORTAL_PROJECTILE);


                PersistentProjectileEntity persistentProjectileEntity = portalProjectile.createPortalProjectile(world, itemStack, playerEntity);
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2F, 0F);
                persistentProjectileEntity.setDamage(0);

                world.spawnEntity(persistentProjectileEntity);

                ((PlayerEntity) user).getItemCooldownManager().set(this, 10);


            }
        }
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

            if (currentPortal == BLUE) {
                tooltip.add(Text.literal("Current Portal: Blue").formatted(Formatting.BLUE));
            }
            else if (currentPortal == ORANGE) {
                tooltip.add(Text.literal("Current Portal: Orange").formatted(Formatting.BLUE));
            }
        }
        else {
            tooltip.add(Text.literal("Press Shift for Portal Color").formatted(Formatting.YELLOW));
        }
    }

    public static void clearPortals() {
        if (blockHitOrange == null && blockHitBlue == null) {
            return;
        }
        else if (blockHitOrange == null) {
            world.setBlockState(blockHitBluePos, blockHitBlue.getDefaultState());
            setBluePortalExists(false);
        }
        else if (blockHitBlue == null) {
            world.setBlockState(blockHitOrangePos, blockHitOrange.getDefaultState());
            setOrangePortalExists(false);
        }
        else {
            world.setBlockState(blockHitBluePos, blockHitBlue.getDefaultState());
            world.setBlockState(blockHitOrangePos, blockHitOrange.getDefaultState());
            setBluePortalExists(false);
            setOrangePortalExists(false);
        }
    }

    public static void clearPortal(int color) {
        if (color == BLUE) {
            world.setBlockState(blockHitBluePos, blockHitBlue.getDefaultState());
            setBluePortalExists(false);
        }
        else if (color == ORANGE) {
            world.setBlockState(blockHitOrangePos, blockHitOrange.getDefaultState());
            setOrangePortalExists(false);
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

    public static boolean isUserEmpty() {
        return user != null;
    }

    public static void sendUpdate(String message) {
        if (isUserEmpty()) {
            user.sendMessage(Text.literal(message));
        }
    }

    public static BlockPos getBluePortalPos() {
        return bluePortalPos;
    }

    public static BlockPos getOrangePortalPos() {
        return orangePortalPos;
    }

    public static void setBluePortalPos(BlockPos pos) {
        PortalGunItem.bluePortalPos = pos;
    }

    public static void setOrangePortalPos(BlockPos pos) {
        PortalGunItem.orangePortalPos = pos;
    }

    public static int getCurrentPortal() {
        return currentPortal;
    }

    public static void setCurrentPortal(int portal) {

        if (portal == BLUE || portal == ORANGE) {
            PortalGunItem.currentPortal = portal;
        }
    }

    public static Block getBlockHitBlue() {
        return blockHitBlue;
    }

    public static Block getBlockHitOrange() {
        return blockHitOrange;
    }

    public static void setBlockHitBlue(Block blockHitBlue) {
        PortalGunItem.blockHitBlue = blockHitBlue;
    }

    public static void setBlockHitOrange(Block blockHitOrange) {
        PortalGunItem.blockHitOrange = blockHitOrange;
    }

    public static boolean isBluePortalExists() {
        return bluePortalExists;
    }

    public static boolean isOrangePortalExists() {
        return orangePortalExists;
    }

    public static void setBluePortalExists(boolean bluePortalExists) {
        PortalGunItem.bluePortalExists = bluePortalExists;
    }

    public static void setOrangePortalExists(boolean orangePortalExists) {
        PortalGunItem.orangePortalExists = orangePortalExists;
    }

    public static BlockPos getBlockHitBluePos() {
        return blockHitBluePos;
    }

    public static BlockPos getBlockHitOrangePos() {
        return blockHitOrangePos;
    }

    public static void setBlockHitBluePos(BlockPos blockHitBluePos) {
        PortalGunItem.blockHitBluePos = blockHitBluePos;
    }

    public static void setBlockHitOrangePos(BlockPos blockHitOrangePos) {
        PortalGunItem.blockHitOrangePos = blockHitOrangePos;
    }
}
