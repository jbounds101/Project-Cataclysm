package net.bird.projectcataclysm.entity.custom;



import com.google.common.collect.Sets;
import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class BulletEntity extends PersistentProjectileEntity {
    public BulletEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntities.BulletEntityType, world);
    }

    public BulletEntity(double x, double y, double z, World world) {
        super(ModEntities.BulletEntityType, x, y, z, world);
    }

    public BulletEntity(LivingEntity owner, World world) {
        super(ModEntities.BulletEntityType, owner, world);
    }

    protected void onHit(LivingEntity target) {
        super.onHit(target);
    }


    protected void initDataTracker() {
        super.initDataTracker();
    }
    protected Item getDefaultItem() {
        return ModItems.BULLET;
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
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.BULLET);
    }



}
