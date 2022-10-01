package net.bird.projectcataclysm.entity.explosive;

import net.bird.projectcataclysm.CustomExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExplosiveBlockEntity extends TntEntity {

    Constructor<? extends CustomExplosion> customExplosionConstructor;

    public ExplosiveBlockEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter, int fuse,
                           Class<CustomExplosion> customExplosion) throws NoSuchMethodException {
        super(world, x, y, z, igniter);
        this.setFuse(fuse);
        this.customExplosionConstructor = customExplosion.getConstructor(World.class, Entity.class, DamageSource.class,
                ExplosionBehavior.class, Double.class, Double.class, Double.class, Float.class, Boolean.class,
                Explosion.DestructionType.class);
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
        CustomExplosion customExplosion = null;
        try {
            //orld world, @Nullable Entity entity, @Nullable DamageSource damageSource,
            //                           @Nullable ExplosionBehavior behavior, double x, double y, double z, float power,
            //                           boolean createFire, DestructionType destructionType
           // this, this.getX(), this.getBodyY(0.0625), this.getZ(), 4.0F, Explosion.DestructionType.BREAK
            customExplosion = customExplosionConstructor.newInstance(world, this, this, null, this.getX(),
                    this.getBodyY(0.0625), this.getZ(), 4.0F, Explosion.DestructionType.BREAK);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        customExplosion.collectBlocksAndDamageEntities();
        customExplosion.affectWorld(true);
    }
}
