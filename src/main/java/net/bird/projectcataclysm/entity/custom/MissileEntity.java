package net.bird.projectcataclysm.entity.custom;


import net.bird.projectcataclysm.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class MissileEntity extends Entity {
    protected boolean inGround;
    protected ExplosiveEntity payload;
    private int explosionPower = 5;
    @Nullable
    private BlockState inBlockState;
    public MissileEntity(EntityType<? extends MissileEntity> entityType, World world) {
        super(entityType, world);
    }

    public MissileEntity(World world, double x, double y, double z) {
        this(ModEntities.MISSILE, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3d vec3d2;
        VoxelShape voxelShape;
        BlockPos blockPos;
        BlockState blockState;
        Vec3d currentVelocity = this.getVelocity();
        if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
            double d = currentVelocity.horizontalLength();
            this.setYaw((float)(MathHelper.atan2(currentVelocity.x, currentVelocity.z) * 57.2957763671875));
            this.setPitch((float)(MathHelper.atan2(currentVelocity.y, d) * 57.2957763671875));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
        if (!((blockState = this.world.getBlockState(blockPos = this.getBlockPos())).isAir() || (voxelShape = blockState.getCollisionShape(this.world, blockPos)).isEmpty())) {
            vec3d2 = this.getPos();
            for (Box box : voxelShape.getBoundingBoxes()) {
                if (!box.offset(blockPos).contains(vec3d2)) continue;
                this.inGround = true;
                break;
            }
        }
        if (this.inGround) {
            if (this.inBlockState != blockState && this.shouldFall()) {
                this.fall();
            }
            return;
        }
        Vec3d vec3d3 = this.getPos();
        HitResult hitResult = this.world.raycast(new RaycastContext(vec3d3, vec3d2 = vec3d3.add(currentVelocity), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d2 = hitResult.getPos();
        }
        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d2);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)hitResult).getEntity();
                if (entity instanceof PlayerEntity) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }
            if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision();
                this.velocityDirty = true;
            }
            if (entityHitResult == null) break;
            hitResult = null;
        }
        currentVelocity = this.getVelocity();
        double velX = currentVelocity.x;
        double velY = currentVelocity.y;
        double velZ = currentVelocity.z;
        double posX = this.getX() + velX;
        double posY = this.getY() + velY;
        double posZ = this.getZ() + velZ;
        double l = currentVelocity.horizontalLength();
        this.setYaw((float)(MathHelper.atan2(velX, velZ) * 57.2957763671875));
        this.setPitch((float)(MathHelper.atan2(velY, l) * 57.2957763671875));
        this.setPitch(MissileEntity.updateRotation(this.prevPitch, this.getPitch()));
        this.setYaw(MissileEntity.updateRotation(this.prevYaw, this.getYaw()));
        Vec3d vec3d4 = this.getVelocity();
        this.setVelocity(vec3d4.x, vec3d4.y - (double)0.05f, vec3d4.z);
        this.setPosition(posX, posY, posZ);
        this.checkBlockCollision();
    }

    protected void onCollision() {
        if (!this.world.isClient) {
            //payload.explode();
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), this.explosionPower, false, Explosion.DestructionType.DESTROY);
            this.discard();
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit);
    }

    protected boolean canHit(Entity entity) {
        return !entity.isSpectator() && entity.isAlive() && entity.canHit();
    }

    private boolean shouldFall() {
        return this.inGround && this.world.isSpaceEmpty(new Box(this.getPos(), this.getPos()).expand(0.06));
    }

    private void fall() {
        this.inGround = false;
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.multiply(this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f));
    }

    public void setPayload(ExplosiveEntity payload) {
        this.payload = payload;
    }

    public void setVelocity(double x, double y, double z, float speed) {
        Vec3d vec3d = new Vec3d(x, y, z).normalize().multiply(speed);
        this.setVelocity(vec3d);
        double d = vec3d.horizontalLength();
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    public void setVelocityRotation(float pitch, float yaw, float roll, float speed) {
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
        float g = -MathHelper.sin((pitch + roll) * ((float)Math.PI / 180));
        float h = MathHelper.cos(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
        this.setVelocity(f, g, h, speed);
    }

    public void setVelocityClient(double x, double y, double z) {
        this.setVelocity(x, y, z);
        if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
            double d = Math.sqrt(x * x + z * z);
            this.setPitch((float)(MathHelper.atan2(y, d) * 57.2957763671875));
            this.setYaw((float)(MathHelper.atan2(x, z) * 57.2957763671875));
            this.prevPitch = this.getPitch();
            this.prevYaw = this.getYaw();
            this.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        }
    }

    protected void updateRotation() {
        Vec3d vec3d = this.getVelocity();
        double d = vec3d.horizontalLength();
        this.setPitch(MissileEntity.updateRotation(this.prevPitch, (float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875)));
        this.setYaw(MissileEntity.updateRotation(this.prevYaw, (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875)));
    }

    protected static float updateRotation(float prevRot, float newRot) {
        while (newRot - prevRot < -180.0f) {
            prevRot -= 360.0f;
        }
        while (newRot - prevRot >= 180.0f) {
            prevRot += 360.0f;
        }
        return MathHelper.lerp(0.2f, prevRot, newRot);
    }

    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

}
