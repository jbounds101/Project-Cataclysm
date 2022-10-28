package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.explosion.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class IceExplosiveEntity extends ExplosiveEntity {

    public IceExplosiveEntity(EntityType<ExplosiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter, ModEntities.ICE_EXPLOSIVE);
    }

    @Override
    public void explode() {
        // TODO Create ice effect
        /*BlockPos positionClicked = context.getBlockPos();
        int x = positionClicked.getX();

        int y = positionClicked.getY() + 1;
        int z = positionClicked.getZ();
        BlockPos positionClicked2 = new BlockPos(x, y, z);
        //positionClicked.add(100, 100, 100);
        float dir = context.getPlayerFacing().asRotation();
        PlayerEntity user = context.getPlayer();
        //user.sendMessage(Text.literal(String.valueOf(dir)));
        if (dir == 0 || dir == 180) {
            for (int x2 = 0; x2 < 3; x2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x + x2, y + y2, z);
                    context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                }
                */
        BlockPos position = new BlockPos(this.getPos());
        CustomExplosion explosion = new CustomExplosion(this.world, this,
                null, position.getX(), position. getY(), position.getZ(), 6);
        explosion.collectBlocksAndGetEntities();
        explosion.affectWorld(true);
    }
}
