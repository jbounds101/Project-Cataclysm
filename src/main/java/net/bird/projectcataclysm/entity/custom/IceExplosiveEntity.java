package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.explosion.IceExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        BlockPos position = new BlockPos(this.getPos());
        IceExplosion explosion = new IceExplosion(this.world, this,
                null, position.getX(), position. getY(), position.getZ(), 6);
        explosion.collectBlocksAndGetEntities();
        explosion.affectWorld();
    }
}
