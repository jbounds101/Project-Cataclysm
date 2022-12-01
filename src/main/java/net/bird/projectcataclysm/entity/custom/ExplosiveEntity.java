package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public abstract class ExplosiveEntity extends TntEntity {
    private static final TrackedData<Integer> FUSE;
    private static final int DEFAULT_FUSE = 80;
    @Nullable
    private LivingEntity causingEntity;

    public ExplosiveEntity(EntityType<? extends TntEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public ExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter,
                           EntityType<? extends ExplosiveEntity> entityType) {
        this(entityType, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.20000000298023224, -Math.cos(d) * 0.02);
        this.setFuse(DEFAULT_FUSE);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }


    protected void initDataTracker() {
        this.dataTracker.startTracking(FUSE, 80);
    }

    protected Entity.MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    public boolean canHit() {
        return !this.isRemoved();
    }

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
                this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5,
                        this.getZ(), 0.0, 0.0, 0.0);
            }
        }

    }

    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putShort("Fuse", (short)this.getFuse());
    }

    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.setFuse(nbt.getShort("Fuse"));
    }

    @Nullable
    public LivingEntity getCausingEntity() {
        return this.causingEntity;
    }

    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15F;
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() == ModItems.DEFUSER) {
            if (this.world.isClient) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH,
                        SoundCategory.BLOCKS, 4.0f, 1.2F, false);
            } else {
                player.getStackInHand(hand).damage(1, player, (p) -> {
                    p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                });
            }
            this.kill();
            return ActionResult.SUCCESS;
            }
        return ActionResult.FAIL;
    }

    public void explode() {
        this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 4.0f,
                Explosion.DestructionType.BREAK);
    }

    static {
        FUSE = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
