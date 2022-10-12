package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class BigExplosiveEntity extends ExplosiveEntity {

    public BigExplosiveEntity(EntityType<ExplosiveEntity> explosiveEntityEntityType, World world) {
        super(explosiveEntityEntityType, world);
    }

    public BigExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter, ModEntities.BIG_EXPLOSIVE);
    }

    @Override
    public void explode() {
        this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 8.0f,
                Explosion.DestructionType.BREAK);
    }

}
