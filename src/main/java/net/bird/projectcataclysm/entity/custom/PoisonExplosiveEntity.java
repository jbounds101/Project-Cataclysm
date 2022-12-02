package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.explosion.PoisonExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PoisonExplosiveEntity extends ExplosiveEntity {

    public PoisonExplosiveEntity(EntityType<ExplosiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public PoisonExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter, ModEntities.POISON_EXPLOSIVE);
    }

    @Override
    public void explode() {
        BlockPos position = new BlockPos(this.getPos());
        PoisonExplosion explosion = new PoisonExplosion(this.world, this,
                null, position.getX(), position. getY(), position.getZ(), 6);
        explosion.collectBlocksAndGetEntities();
        explosion.affectWorld();
    }
}
