package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SlugEntity extends PersistentProjectileEntity {
    public SlugEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntities.SlugEntityType, world);
    }

    public SlugEntity(double x, double y, double z, World world) {
        super(ModEntities.SlugEntityType, x, y, z, world);
    }

    public SlugEntity(LivingEntity owner, World world) {
        super(ModEntities.SlugEntityType, owner, world);
    }

    protected void onHit(LivingEntity target) {
        super.onHit(target);
    }

    protected void initDataTracker() {
        super.initDataTracker();
    }
    protected Item getDefaultItem() {
        return ModItems.SLUG;
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

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        Vec3d vec3d = entity.getPos();
        assert owner != null;
        Vec3d ownerVec3d = owner.getPos();

        if (ownerVec3d.distanceTo(vec3d) <= 8) {
            super.onEntityHit(entityHitResult);
        }

        //super.onEntityHit(entityHitResult);
    }

    public void tick() {
        super.tick();
    }

    @Override
    protected boolean tryPickup(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }
}
