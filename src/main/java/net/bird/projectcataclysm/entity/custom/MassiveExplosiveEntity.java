package net.bird.projectcataclysm.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class MassiveExplosiveEntity extends ExplosiveEntity {

    public MassiveExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter);
    }

    @Override
    public void explode() {
        this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 128.0f,
                Explosion.DestructionType.DESTROY);
    }
}
