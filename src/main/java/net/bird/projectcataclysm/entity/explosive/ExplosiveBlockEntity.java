package net.bird.projectcataclysm.entity.explosive;

import net.bird.projectcataclysm.CustomExplosion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.TntEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExplosiveBlockEntity extends TntEntity {

    CustomExplosion customExplosion;

    public ExplosiveBlockEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter, int fuse,
                           CustomExplosion customExplosion) {
        super(world, x, y, z, igniter);
        this.setFuse(fuse);
        this.customExplosion = customExplosion;
    }

    public ExplosiveBlockEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter,
                                CustomExplosion customExplosion) {
        super(world, x, y, z, igniter);
        this.setFuse(80);
        this.customExplosion = customExplosion;
    }

    // TODO Check if this is needed
    public void tick() {
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98));
        if (this.onGround) {
            this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.world.isClient) {
                this.explode();
            }
        } else {
            this.updateWaterState();
            if (this.world.isClient) {
                this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }

    }

    private void explode() {
        customExplosion.collectBlocksAndDamageEntities();
        customExplosion.affectWorld(true);
    }
}
