package net.bird.projectcataclysm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class BigExplosiveEntity extends ExplosiveEntity {
    public BigExplosiveEntity(EntityType<? extends TntEntity> entityType, World world) {
        super(entityType, world);
    }

    public BigExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter);
    }

    public BigExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter, int fuse) {
        super(world, x, y, z, igniter, fuse);
    }

    @Override
    public void explode() {
        this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 8.0f,
                Explosion.DestructionType.BREAK);
    }
}
