package net.bird.projectcataclysm.entity.explosive;

import net.bird.projectcataclysm.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LargeTNTEntity extends ExplosiveBlockEntity {


    public LargeTNTEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter, int fuse,
                          Class<CustomExplosion> customExplosion) throws NoSuchMethodException {
        super(world, x, y, z, igniter, fuse, customExplosion);
    }
}
