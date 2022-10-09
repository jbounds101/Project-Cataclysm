package net.bird.projectcataclysm.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class CustomExplosion extends Explosion {

    public CustomExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power) {
        super(world, entity, x, y, z, power);
    }
}
