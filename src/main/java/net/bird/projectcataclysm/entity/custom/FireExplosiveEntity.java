package net.bird.projectcataclysm.entity.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FireExplosiveEntity extends ExplosiveEntity {
    public FireExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter);
    }

    @Override
    public void explode() {
    }
}
