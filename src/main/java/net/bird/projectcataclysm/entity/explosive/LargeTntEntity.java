package net.bird.projectcataclysm.entity.explosive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class LargeTntEntity extends TntEntity {

    public LargeTntEntity(EntityType<? extends TntEntity> entityType, World world) {
        super(entityType, world);
    }

    public LargeTntEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(world, x, y, z, igniter);
    }

    // This does nothing for some reason
    @Override
    private void explode() {
        this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 12.0F,
                Explosion.DestructionType.BREAK);
        this.world.spawnEntity(new ZombieEntity(this.world));
    }
}
