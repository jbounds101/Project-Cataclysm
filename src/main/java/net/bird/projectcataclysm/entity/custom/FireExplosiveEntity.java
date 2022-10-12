package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class FireExplosiveEntity extends ExplosiveEntity {

    public FireExplosiveEntity(EntityType<ExplosiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public FireExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter, ModEntities.FIRE_EXPLOSIVE);
    }

    @Override
    public void explode() {
        this.world.createExplosion(this, null, null, this.getX(),
                this.getBodyY(0.0625), this.getZ(), 8.0f, true,
                Explosion.DestructionType.NONE);
    }

}
