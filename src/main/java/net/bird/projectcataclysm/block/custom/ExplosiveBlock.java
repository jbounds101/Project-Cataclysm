package net.bird.projectcataclysm.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ExplosiveBlock extends TntBlock {

    // When a TntBlock is ignited, it calls prime(), which creates a TntEntity

    public ExplosiveBlock(Settings settings) {
        super(settings);
    }

    public static void primeExplosive(World world, BlockPos pos) {
        ExplosiveBlock.primeExplosive(world, pos, null);
    }

    private static void primeExplosive(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5,
                igniter);
        world.spawnEntity(tntEntity);

        // Play sound event
        //world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED,
                //SoundCategory.BLOCKS, 1.0f, 1.0f);
        // TODO remove this, used in testing
        world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_AXOLOTL_DEATH,
                SoundCategory.BLOCKS, 1.0f, 4.0f);

        // Create game event
        world.emitGameEvent((Entity)igniter, GameEvent.PRIME_FUSE, pos);
    }


    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        if (world.isReceivingRedstonePower(pos)) {
            // Explosive was primed with redstone
            ExplosiveBlock.primeExplosive(world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
                               BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            // Explosive was primed with redstone
            ExplosiveBlock.primeExplosive(world, pos);
            world.removeBlock(pos, false);
        }
    }

    // Not entirely sure what this one does, but it needs to be overriden regardless to use the ExplosiveBlock
    // .primeExplosive method rather than primeTnt
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && !player.isCreative() && state.get(UNSTABLE).booleanValue()) {
            ExplosiveBlock.primeExplosive(world, pos);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player2, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player2.getStackInHand(hand);
        if (itemStack.isOf(Items.FLINT_AND_STEEL) || itemStack.isOf(Items.FIRE_CHARGE)) {
            ExplosiveBlock.primeExplosive(world, pos, player2);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            Item item = itemStack.getItem();
            if (!player2.isCreative()) {
                if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
                    itemStack.damage(1, player2, player -> player.sendToolBreakStatus(hand));
                } else {
                    itemStack.decrement(1);
                }
            }
            player2.incrementStat(Stats.USED.getOrCreateStat(item));
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player2, hand, hit);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.canModifyAt(world, blockPos)) {
                ExplosiveBlock.primeExplosive(world, blockPos,
                        entity instanceof LivingEntity ? (LivingEntity)entity : null);
                world.removeBlock(blockPos, false);
            }
        }
    }
}
