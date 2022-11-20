package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExplosiveProjectileEntity extends PersistentProjectileEntity {
    public ExplosiveProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntities.ExplosiveProjectileEntityType, world);
    }

    public ExplosiveProjectileEntity(double x, double y, double z, World world) {
        super(ModEntities.ExplosiveProjectileEntityType, x, y, z, world);
    }

    public ExplosiveProjectileEntity(LivingEntity owner, World world) {
        super(ModEntities.ExplosiveProjectileEntityType, owner, world);
    }

    protected void onHit(LivingEntity target) {
        if (!this.world.isClient()) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 5, Explosion.DestructionType.DESTROY);
            this.discard();
        }
        super.onHit(target);
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = ParticleTypes.ENTITY_EFFECT;

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);

            }
        }

    }



    public void tick() {
        super.tick();
    }

    @Override
    protected boolean tryPickup(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.world.isClient()) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 5.0F, Explosion.DestructionType.DESTROY);
            this.discard();
        }
        //super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.world.isClient()) {
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 5.0F, Explosion.DestructionType.DESTROY);
            this.discard();
        }

        //super.onEntityHit(entityHitResult);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }
}
