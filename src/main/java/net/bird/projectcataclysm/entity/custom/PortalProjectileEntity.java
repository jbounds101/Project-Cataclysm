package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.item.custom.PortalGunItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PortalProjectileEntity extends PersistentProjectileEntity {

    static Block blockHitBlue;

    static Block blockHitOrange;

    static int BLUE = PortalGunItem.BLUE;
    static int ORANGE = PortalGunItem.ORANGE;



    static BlockPos bluePortalPos;
    static BlockPos orangePortalPos;

    public PortalProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntities.PortalProjectileEntityType, world);
    }

    public PortalProjectileEntity(double x, double y, double z, World world) {
        super(ModEntities.PortalProjectileEntityType, x, y, z, world);
    }

    public PortalProjectileEntity(LivingEntity owner, World world) {
        super(ModEntities.PortalProjectileEntityType, owner, world);
    }




    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

        Block stateCheck = this.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
        int currentPortal = PortalGunItem.getCurrentPortal();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        if (!this.world.isClient()) {
            if (!stateCheck.equals(Blocks.WATER) && !stateCheck.equals(Blocks.LAVA) &&
                    !stateCheck.equals(Blocks.BEDROCK) && !stateCheck.equals(Blocks.CHEST)) {
                if (blockHitResult.getBlockPos().getY() < this.getOwner().getPos().getY()) {
                    // Save current block
                    if (currentPortal == BLUE) {
                        // .setBlockState for portal block
                        if (!PortalGunItem.isBluePortalExists()) {
                            blockHitBlue = this.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
                            PortalGunItem.setBlockHitBlue(blockHitBlue);
                            PortalGunItem.setBlockHitBluePos(blockHitResult.getBlockPos());
                            this.world.setBlockState(blockHitResult.getBlockPos(), ModBlocks.BLUE_PORTAL_BLOCK.getDefaultState());
                            bluePortalPos = blockHitResult.getBlockPos();
                            PortalGunItem.setBluePortalPos(bluePortalPos);
                            PortalGunItem.setBluePortalExists(true);
                        } else {
                            PortalGunItem.clearPortal(PortalGunItem.BLUE);
                            blockHitBlue = this.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
                            PortalGunItem.setBlockHitBlue(blockHitBlue);
                            PortalGunItem.setBlockHitBluePos(blockHitResult.getBlockPos());


                            this.world.setBlockState(blockHitResult.getBlockPos(), ModBlocks.BLUE_PORTAL_BLOCK.getDefaultState());
                            bluePortalPos = blockHitResult.getBlockPos();
                            PortalGunItem.setBluePortalPos(bluePortalPos);
                            PortalGunItem.setBluePortalExists(true);
                        }

                    } else if (currentPortal == ORANGE) {

                        // .setBlockState for portal block
                        if (!PortalGunItem.isOrangePortalExists()) {
                            blockHitOrange = this.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
                            PortalGunItem.setBlockHitOrange(blockHitOrange);
                            PortalGunItem.setBlockHitOrangePos(blockHitResult.getBlockPos());
                            this.world.setBlockState(blockHitResult.getBlockPos(), ModBlocks.ORANGE_PORTAL_BLOCK.getDefaultState());
                            orangePortalPos = blockHitResult.getBlockPos();
                            PortalGunItem.setOrangePortalPos(orangePortalPos);
                            PortalGunItem.setOrangePortalExists(true);
                        } else {
                            PortalGunItem.clearPortal(PortalGunItem.ORANGE);
                            blockHitOrange = this.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
                            PortalGunItem.setBlockHitOrange(blockHitOrange);
                            PortalGunItem.setBlockHitOrangePos(blockHitResult.getBlockPos());


                            this.world.setBlockState(blockHitResult.getBlockPos(), ModBlocks.ORANGE_PORTAL_BLOCK.getDefaultState());
                            orangePortalPos = blockHitResult.getBlockPos();
                            PortalGunItem.setOrangePortalPos(orangePortalPos);
                            PortalGunItem.setOrangePortalExists(true);
                        }
                        // save location

                    }
                }
            }
        }
        this.discard();
        //super.onBlockHit(blockHitResult);
    }


    protected boolean tryPickup(PlayerEntity playerEntity) {
        return false;
    }

    protected static void doNothing() {
        return;
    }



    @Override
    protected ItemStack asItemStack() {
        return null;
    }
}
