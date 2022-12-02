package net.bird.projectcataclysm.explosion;


import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.custom.ExplosiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WaterExplosion {

    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();
    private static final int field_30960 = 16;
    private final Random random = Random.create();
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity entity;
    private final float power;
    private final ExplosionBehavior behavior;
    private final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList();
    private List<Entity> affectedEntities = new ArrayList<>() {
    };


    public WaterExplosion(World world, @Nullable Entity entity, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power) {
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.x = x;
        this.y = y;
        this.z = z;
        this.behavior = behavior == null ? this.chooseBehavior(entity) : behavior;
    }

    private ExplosionBehavior chooseBehavior(@Nullable Entity entity) {
        return entity == null ? DEFAULT_BEHAVIOR : new EntityExplosionBehavior(entity);
    }

    public static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double p;
                    double o;
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    Vec3d vec3d = new Vec3d(n + g, o = MathHelper.lerp(l, box.minY, box.maxY), (p = MathHelper.lerp(m, box.minZ, box.maxZ)) + h);
                    if (entity.world.raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    public void collectBlocksAndGetEntities() {
        int l;
        int k;
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        HashSet<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                block2: for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float)j / 15.0f * 2.0f - 1.0f;
                    double e = (float)k / 15.0f * 2.0f - 1.0f;
                    double f = (float)l / 15.0f * 2.0f - 1.0f;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = this.x;
                    double n = this.y;
                    double o = this.z;
                    float p = 0.3f;
                    for (float h = this.power * (0.7f + this.world.random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = new BlockPos(m, n, o);
                        BlockState blockState = this.world.getBlockState(blockPos);
                        FluidState fluidState = this.world.getFluidState(blockPos);
                        if (!this.world.isInBuildLimit(blockPos)) continue block2;

                        if (h > 0.0f) { // && this.behavior.canDestroyBlock(this, this.world, blockPos, blockState, h)
                            set.add(blockPos);
                        }
                        m += d * (double)0.3f;
                        n += e * (double)0.3f;
                        o += f * (double)0.3f;
                    }
                }
            }
        }
        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0f; // This is the "damage-radius" for entities
        k = MathHelper.floor(this.x - (double)q - 1.0);
        l = MathHelper.floor(this.x + (double)q + 1.0);
        int r = MathHelper.floor(this.y - (double)q - 1.0);
        int s = MathHelper.floor(this.y + (double)q + 1.0);
        int t = MathHelper.floor(this.z - (double)q - 1.0);
        int u = MathHelper.floor(this.z + (double)q + 1.0);
        affectedEntities = this.world.getOtherEntities(this.entity, new Box(k, r, t, l, s, u));


    }

    public void affectWorld() {
        if (!this.world.isClient) {
            if (this.world.getRegistryKey() == World.NETHER) {
                this.world.playSound(null, new BlockPos(this.x, this.y, this.z), SoundEvents.BLOCK_LAVA_EXTINGUISH,
                        SoundCategory.BLOCKS, 4.0f, 1.2F);
                return;
            } else {
                this.world.playSound(null, new BlockPos(this.x, this.y, this.z), SoundEvents.AMBIENT_UNDERWATER_ENTER,
                        SoundCategory.BLOCKS, 4.0f, 1.2F);
                this.world.playSound(null, new BlockPos(this.x, this.y, this.z), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                        SoundCategory.BLOCKS, 4.0f, 1.2F);
            }

        }

        ArrayList<EntityType> seaCreatures = new ArrayList<>();
        seaCreatures.add(EntityType.TROPICAL_FISH);
        seaCreatures.add(EntityType.PUFFERFISH);
        seaCreatures.add(EntityType.DOLPHIN);
        seaCreatures.add(EntityType.TURTLE);
        seaCreatures.add(EntityType.SQUID);
        seaCreatures.add(EntityType.GLOW_SQUID);
        // This loops through the "affectedBlocks" from the explosion
        for (BlockPos blockPos : this.affectedBlocks) {
            if (blockPos.getY() > this.y) continue;
            BlockState blockState = this.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if ((block.getClass() == ExplosiveBlock.class) || (block.getClass() == TntBlock.class)) {
                block.onDestroyedByExplosion(this.world, blockPos, null);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState()); // Delete a explosion block
            }

           // if (this.random.nextInt(20) != 0 || !this.world.getBlockState(blockPos).isAir() || !this.world
            // .getBlockState(blockPos.down()).isOpaqueFullCube(this.world, blockPos.down())) continue;

            world.setBlockState(blockPos, Blocks.WATER.getDefaultState());

            if (this.random.nextInt(120) == 0) {
                // Decides if a sea-creature should be spawned at this position
                Entity entity = seaCreatures.get(random.nextBetween(0, seaCreatures.size() - 1)).create(world);
                assert entity != null;
                entity.refreshPositionAfterTeleport(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                world.spawnEntity(entity);
            }
        }
    }



    public void clearAffectedBlocks() {
        this.affectedBlocks.clear();
    }

    public List<BlockPos> getAffectedBlocks() {
        return this.affectedBlocks;
    }

    /*public static enum DestructionType {
        NONE,
        BREAK,
        DESTROY;

    }*/
}
