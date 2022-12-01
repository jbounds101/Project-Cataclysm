package net.bird.projectcataclysm.explosion;


import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.bird.projectcataclysm.block.custom.ExplosiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NatureExplosion {

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


    public NatureExplosion(World world, @Nullable Entity entity, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power) {
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
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
        } else {
            if (this.world.getRegistryKey() != World.OVERWORLD) {
                this.world.playSound(null, new BlockPos(this.x, this.y, this.z), SoundEvents.BLOCK_LAVA_EXTINGUISH,
                        SoundCategory.BLOCKS, 4.0f, 1.2F);
                return;
            }
        }






        ArrayList<Block> plants = new ArrayList<>();
        plants.add(Blocks.DANDELION);
        plants.add(Blocks.POPPY);
        plants.add(Blocks.BLUE_ORCHID);
        plants.add(Blocks.ALLIUM);
        plants.add(Blocks.AZURE_BLUET);
        plants.add(Blocks.ORANGE_TULIP);
        plants.add(Blocks.PINK_TULIP);
        plants.add(Blocks.RED_TULIP);
        plants.add(Blocks.WHITE_TULIP);
        plants.add(Blocks.OXEYE_DAISY);
        plants.add(Blocks.CORNFLOWER);
        plants.add(Blocks.LILY_OF_THE_VALLEY);
        plants.add(Blocks.SUNFLOWER);
        plants.add(Blocks.LILAC);
        plants.add(Blocks.ROSE_BUSH);
        plants.add(Blocks.PEONY);
        plants.add(Blocks.OAK_SAPLING);
        plants.add(Blocks.BIRCH_SAPLING);

        HashSet<Block> possibleFlowerBlocks = new HashSet<>();
        possibleFlowerBlocks.add(Blocks.GRASS_BLOCK);
        possibleFlowerBlocks.add(Blocks.DIRT);
        possibleFlowerBlocks.add(Blocks.COARSE_DIRT);
        possibleFlowerBlocks.add(Blocks.MOSS_BLOCK);
        possibleFlowerBlocks.add(Blocks.MUD);

        boolean beeHiveCreated = false;
        int beesRemaining = 2;

        // This loops through the "affectedBlocks" from the explosion
        for (BlockPos blockPos : this.affectedBlocks) {

            BlockState blockState = this.world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            if ((block.getClass() == ExplosiveBlock.class) || (block.getClass() == TntBlock.class)) {
                block.onDestroyedByExplosion(this.world, blockPos, null);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState()); // Delete a explosion block
            }

            if (!possibleFlowerBlocks.contains(this.world.getBlockState(blockPos.down()).getBlock())) continue;

            if (this.random.nextInt(5) != 0 || !this.world.getBlockState(blockPos).isAir() || !this.world.getBlockState(blockPos.down()).isOpaqueFullCube(this.world, blockPos.down())) continue;
            // This block isn't a possible candidate for flower placing

            if (!beeHiveCreated) {
                world.setBlockState(blockPos, Blocks.BEE_NEST.getDefaultState());
                beeHiveCreated = true;
                continue;
            }

            // Decides if a flower should be spawned at this position
            world.setBlockState(blockPos, plants.get(random.nextBetween(0, plants.size() - 1)).getDefaultState());
            if (beesRemaining > 0) {
                BeeEntity beeEntity = EntityType.BEE.create(world);
                assert beeEntity != null;
                beeEntity.refreshPositionAfterTeleport(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                world.spawnEntity(beeEntity);
                beesRemaining--;
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
