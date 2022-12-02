package net.bird.projectcataclysm.explosion;


import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.bird.projectcataclysm.block.custom.ExplosiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EarthExplosion {

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


    public EarthExplosion(World world, @Nullable Entity entity, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power) {
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
                        if (h > 0.0f) {
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
        }
        this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);

        BlockPos pos = this.entity.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        //y = y - 8;
        int z = pos.getZ();
        z = z-6;

        BlockPos pos2 = new BlockPos(x + 3, y, z);
        this.world.setBlockState(pos2, Blocks.DIRT.getDefaultState());
        BlockPos pos3 = new BlockPos(x + 3, y, z+1);
        this.world.setBlockState(pos3, Blocks.STONE.getDefaultState());
        BlockPos pos4 = new BlockPos(x + 3, y, z-1);
        this.world.setBlockState(pos4, Blocks.DIRT.getDefaultState());
        BlockPos pos5 = new BlockPos(x + 2, y, z-2);
        this.world.setBlockState(pos5, Blocks.DIRT.getDefaultState());
        BlockPos pos6 = new BlockPos(x + 2, y, z+2);
        this.world.setBlockState(pos6, Blocks.STONE.getDefaultState());
        BlockPos pos7 = new BlockPos(x + 1, y, z+3);
        this.world.setBlockState(pos7, Blocks.DIRT.getDefaultState());
        BlockPos pos8 = new BlockPos(x + 1, y, z-3);
        this.world.setBlockState(pos8, Blocks.STONE.getDefaultState());
        BlockPos pos9 = new BlockPos(x, y, z-3);
        this.world.setBlockState(pos9, Blocks.STONE.getDefaultState());
        BlockPos pos10 = new BlockPos(x-1, y, z-3);
        this.world.setBlockState(pos10, Blocks.DIRT.getDefaultState());
        BlockPos pos11 = new BlockPos(x-2, y, z-2);
        this.world.setBlockState(pos11, Blocks.DIRT.getDefaultState());
        BlockPos pos12 = new BlockPos(x-3, y, z-1);
        this.world.setBlockState(pos12, Blocks.DIRT.getDefaultState());
        BlockPos pos13 = new BlockPos(x-3, y, z);
        this.world.setBlockState(pos13, Blocks.STONE.getDefaultState());
        BlockPos pos14 = new BlockPos(x-3, y, z+1);
        this.world.setBlockState(pos14, Blocks.DIRT.getDefaultState());
        BlockPos pos15 = new BlockPos(x-2, y, z+2);
        this.world.setBlockState(pos15, Blocks.STONE.getDefaultState());
        BlockPos pos16 = new BlockPos(x-1, y, z+3);
        this.world.setBlockState(pos16, Blocks.DIRT.getDefaultState());
        BlockPos pos17 = new BlockPos(x, y, z+3);
        this.world.setBlockState(pos17, Blocks.DIRT.getDefaultState());

        BlockPos pos18 = new BlockPos(x+2, y+1, z);
        this.world.setBlockState(pos18, Blocks.DIRT.getDefaultState());
        BlockPos pos19 = new BlockPos(x+2, y+1, z+1);
        this.world.setBlockState(pos19, Blocks.DIRT.getDefaultState());
        BlockPos pos20 = new BlockPos(x+2, y+1, z-1);
        this.world.setBlockState(pos20, Blocks.STONE.getDefaultState());

        BlockPos pos21 = new BlockPos(x, y+1, z+2);
        this.world.setBlockState(pos21, Blocks.STONE.getDefaultState());
        BlockPos pos22 = new BlockPos(x+1, y+1, z+2);
        this.world.setBlockState(pos22, Blocks.DIRT.getDefaultState());
        BlockPos pos23 = new BlockPos(x-1, y+1, z+2);
        this.world.setBlockState(pos23, Blocks.STONE.getDefaultState());

        BlockPos pos24 = new BlockPos(x, y+1, z-2);
        this.world.setBlockState(pos24, Blocks.STONE.getDefaultState());
        BlockPos pos25 = new BlockPos(x+1, y+1, z-2);
        this.world.setBlockState(pos25, Blocks.DIRT.getDefaultState());
        BlockPos pos26 = new BlockPos(x-1, y+1, z-2);
        this.world.setBlockState(pos26, Blocks.DIRT.getDefaultState());

        BlockPos pos27 = new BlockPos(x-2, y+1, z);
        this.world.setBlockState(pos27, Blocks.DIRT.getDefaultState());
        BlockPos pos28 = new BlockPos(x-2, y+1, z+1);
        this.world.setBlockState(pos28, Blocks.STONE.getDefaultState());
        BlockPos pos29 = new BlockPos(x-2, y+1, z-1);
        this.world.setBlockState(pos29, Blocks.DIRT.getDefaultState());

        BlockPos pos30 = new BlockPos(x+1, y+2, z);
        this.world.setBlockState(pos30, Blocks.STONE.getDefaultState());
        BlockPos pos31 = new BlockPos(x+1, y+2, z-1);
        this.world.setBlockState(pos31, Blocks.DIRT.getDefaultState());
        BlockPos pos32 = new BlockPos(x+1, y+2, z+1);
        this.world.setBlockState(pos32, Blocks.DIRT.getDefaultState());

        BlockPos pos33 = new BlockPos(x, y+2, z+1);
        this.world.setBlockState(pos33, Blocks.DIRT.getDefaultState());
        BlockPos pos34 = new BlockPos(x+1, y+2, z+1);
        this.world.setBlockState(pos34, Blocks.STONE.getDefaultState());
        BlockPos pos35 = new BlockPos(x-1, y+2, z+1);
        this.world.setBlockState(pos35, Blocks.DIRT.getDefaultState());

        BlockPos pos36 = new BlockPos(x, y+2, z-1);
        this.world.setBlockState(pos36, Blocks.STONE.getDefaultState());
        BlockPos pos37 = new BlockPos(x+1, y+2, z-1);
        this.world.setBlockState(pos37, Blocks.DIRT.getDefaultState());
        BlockPos pos38 = new BlockPos(x-1, y+2, z-1);
        this.world.setBlockState(pos38, Blocks.STONE.getDefaultState());
        BlockPos pos39 = new BlockPos(x-1, y+2, z);
        this.world.setBlockState(pos39, Blocks.DIRT.getDefaultState());
        BlockPos pos40 = new BlockPos(x, y+3, z);
        this.world.setBlockState(pos40, Blocks.STONE.getDefaultState());








        BlockPos pos41 = new BlockPos(x + 10, y, z+7);
        this.world.setBlockState(pos41, Blocks.DIRT.getDefaultState());
        BlockPos pos42 = new BlockPos(x + 10, y, z+8);
        this.world.setBlockState(pos42, Blocks.STONE.getDefaultState());
        BlockPos pos43 = new BlockPos(x + 10, y, z+6);
        this.world.setBlockState(pos43, Blocks.DIRT.getDefaultState());
        BlockPos pos44 = new BlockPos(x + 9, y, z-5);
        this.world.setBlockState(pos44, Blocks.DIRT.getDefaultState());
        BlockPos pos45 = new BlockPos(x + 9, y, z+7);
        this.world.setBlockState(pos45, Blocks.STONE.getDefaultState());
        BlockPos pos46 = new BlockPos(x + 8, y, z+10);
        this.world.setBlockState(pos46, Blocks.DIRT.getDefaultState());
        BlockPos pos47 = new BlockPos(x + 8, y, z+4);
        this.world.setBlockState(pos47, Blocks.STONE.getDefaultState());
        BlockPos pos48 = new BlockPos(x+7, y, z+4);
        this.world.setBlockState(pos48, Blocks.STONE.getDefaultState());
        BlockPos pos49 = new BlockPos(x+6, y, z+4);
        this.world.setBlockState(pos49, Blocks.DIRT.getDefaultState());
        BlockPos pos50 = new BlockPos(x+5, y, z+5);
        this.world.setBlockState(pos50, Blocks.DIRT.getDefaultState());
        BlockPos pos51 = new BlockPos(x+4, y, z+6);
        this.world.setBlockState(pos51, Blocks.DIRT.getDefaultState());
        BlockPos pos52 = new BlockPos(x+4, y, z+7);
        this.world.setBlockState(pos52, Blocks.STONE.getDefaultState());
        BlockPos pos53 = new BlockPos(x+4, y, z+8);
        this.world.setBlockState(pos53, Blocks.DIRT.getDefaultState());
        BlockPos pos54 = new BlockPos(x+5, y, z+9);
        this.world.setBlockState(pos54, Blocks.STONE.getDefaultState());
        BlockPos pos55 = new BlockPos(x+6, y, z+10);
        this.world.setBlockState(pos55, Blocks.DIRT.getDefaultState());
        BlockPos pos56 = new BlockPos(x+7, y, z+10);
        this.world.setBlockState(pos56, Blocks.DIRT.getDefaultState());

        BlockPos pos57 = new BlockPos(x+9, y+1, z+7);
        this.world.setBlockState(pos57, Blocks.DIRT.getDefaultState());
        BlockPos pos58 = new BlockPos(x+9, y+1, z+8);
        this.world.setBlockState(pos58, Blocks.DIRT.getDefaultState());
        BlockPos pos59 = new BlockPos(x+9, y+1, z+6);
        this.world.setBlockState(pos59, Blocks.STONE.getDefaultState());

        BlockPos pos60 = new BlockPos(x+7, y+1, z+9);
        this.world.setBlockState(pos60, Blocks.STONE.getDefaultState());
        BlockPos pos61 = new BlockPos(x+8, y+1, z+9);
        this.world.setBlockState(pos61, Blocks.DIRT.getDefaultState());
        BlockPos pos62 = new BlockPos(x+6, y+1, z+9);
        this.world.setBlockState(pos62, Blocks.STONE.getDefaultState());

        BlockPos pos63 = new BlockPos(x+7, y+1, z+5);
        this.world.setBlockState(pos63, Blocks.STONE.getDefaultState());
        BlockPos pos64 = new BlockPos(x+8, y+1, z+5);
        this.world.setBlockState(pos64, Blocks.DIRT.getDefaultState());
        BlockPos pos65 = new BlockPos(x+6, y+1, z+5);
        this.world.setBlockState(pos65, Blocks.DIRT.getDefaultState());

        BlockPos pos66 = new BlockPos(x+5, y+1, z+7);
        this.world.setBlockState(pos66, Blocks.DIRT.getDefaultState());
        BlockPos pos67 = new BlockPos(x+5, y+1, z+8);
        this.world.setBlockState(pos67, Blocks.STONE.getDefaultState());
        BlockPos pos68 = new BlockPos(x+5, y+1, z+6);
        this.world.setBlockState(pos68, Blocks.DIRT.getDefaultState());

        BlockPos pos69 = new BlockPos(x+8, y+2, z+7);
        this.world.setBlockState(pos69, Blocks.STONE.getDefaultState());
        BlockPos pos70 = new BlockPos(x+8, y+2, z+6);
        this.world.setBlockState(pos70, Blocks.DIRT.getDefaultState());
        BlockPos pos71 = new BlockPos(x+8, y+2, z+8);
        this.world.setBlockState(pos71, Blocks.DIRT.getDefaultState());

        BlockPos pos72 = new BlockPos(x+7, y+2, z+8);
        this.world.setBlockState(pos72, Blocks.DIRT.getDefaultState());
        BlockPos pos73 = new BlockPos(x+8, y+2, z+8);
        this.world.setBlockState(pos73, Blocks.STONE.getDefaultState());
        BlockPos pos74 = new BlockPos(x+6, y+2, z+8);
        this.world.setBlockState(pos74, Blocks.DIRT.getDefaultState());

        BlockPos pos75 = new BlockPos(x+7, y+2, z+6);
        this.world.setBlockState(pos75, Blocks.STONE.getDefaultState());
        BlockPos pos76 = new BlockPos(x+8, y+2, z+6);
        this.world.setBlockState(pos76, Blocks.DIRT.getDefaultState());
        BlockPos pos77 = new BlockPos(x+6, y+2, z+6);
        this.world.setBlockState(pos77, Blocks.STONE.getDefaultState());
        BlockPos pos78 = new BlockPos(x+6, y+2, z+7);
        this.world.setBlockState(pos78, Blocks.DIRT.getDefaultState());
        BlockPos pos79 = new BlockPos(x+7, y+3, z+7);
        this.world.setBlockState(pos79, Blocks.STONE.getDefaultState());


        BlockPos poss = this.entity.getBlockPos();
        int x2 = poss.getX();
        x2 = x2-4;
        int y2 = poss.getY();
        int z2 = poss.getZ();
        z2 = z2+4;

        BlockPos poss2 = new BlockPos(x2 + 3, y2, z2);
        this.world.setBlockState(poss2, Blocks.DIRT.getDefaultState());
        BlockPos poss3 = new BlockPos(x2 + 3, y2, z2+1);
        this.world.setBlockState(poss3, Blocks.STONE.getDefaultState());
        BlockPos poss4 = new BlockPos(x2 + 3, y2, z2-1);
        this.world.setBlockState(poss4, Blocks.DIRT.getDefaultState());
        BlockPos poss5 = new BlockPos(x2 + 2, y2, z2-2);
        this.world.setBlockState(poss5, Blocks.DIRT.getDefaultState());
        BlockPos poss6 = new BlockPos(x2 + 2, y2, z2+2);
        this.world.setBlockState(poss6, Blocks.STONE.getDefaultState());
        BlockPos poss7 = new BlockPos(x2 + 1, y2, z2+3);
        this.world.setBlockState(poss7, Blocks.DIRT.getDefaultState());
        BlockPos poss8 = new BlockPos(x2 + 1, y2, z2-3);
        this.world.setBlockState(poss8, Blocks.STONE.getDefaultState());
        BlockPos poss9 = new BlockPos(x2, y2, z2-3);
        this.world.setBlockState(poss9, Blocks.STONE.getDefaultState());
        BlockPos poss10 = new BlockPos(x2-1, y2, z2-3);
        this.world.setBlockState(poss10, Blocks.DIRT.getDefaultState());
        BlockPos poss11 = new BlockPos(x2-2, y2, z2-2);
        this.world.setBlockState(poss11, Blocks.DIRT.getDefaultState());
        BlockPos poss12 = new BlockPos(x2-3, y2, z2-1);
        this.world.setBlockState(poss12, Blocks.DIRT.getDefaultState());
        BlockPos poss13 = new BlockPos(x2-3, y2, z2);
        this.world.setBlockState(poss13, Blocks.STONE.getDefaultState());
        BlockPos poss14 = new BlockPos(x2-3, y2, z2+1);
        this.world.setBlockState(poss14, Blocks.DIRT.getDefaultState());
        BlockPos poss15 = new BlockPos(x2-2, y2, z2+2);
        this.world.setBlockState(poss15, Blocks.STONE.getDefaultState());
        BlockPos poss16 = new BlockPos(x2-1, y2, z2+3);
        this.world.setBlockState(poss16, Blocks.DIRT.getDefaultState());
        BlockPos poss17 = new BlockPos(x2, y2, z2+3);
        this.world.setBlockState(poss17, Blocks.DIRT.getDefaultState());

        BlockPos poss18 = new BlockPos(x2+2, y2+1, z2);
        this.world.setBlockState(poss18, Blocks.DIRT.getDefaultState());
        BlockPos poss19 = new BlockPos(x2+2, y2+1, z2+1);
        this.world.setBlockState(poss19, Blocks.DIRT.getDefaultState());
        BlockPos poss20 = new BlockPos(x2+2, y2+1, z2-1);
        this.world.setBlockState(poss20, Blocks.STONE.getDefaultState());

        BlockPos poss21 = new BlockPos(x2, y2+1, z2+2);
        this.world.setBlockState(poss21, Blocks.STONE.getDefaultState());
        BlockPos poss22 = new BlockPos(x2+1, y2+1, z2+2);
        this.world.setBlockState(poss22, Blocks.DIRT.getDefaultState());
        BlockPos poss23 = new BlockPos(x2-1, y2+1, z2+2);
        this.world.setBlockState(poss23, Blocks.STONE.getDefaultState());

        BlockPos poss24 = new BlockPos(x2, y2+1, z2-2);
        this.world.setBlockState(poss24, Blocks.STONE.getDefaultState());
        BlockPos poss25 = new BlockPos(x2+1, y2+1, z2-2);
        this.world.setBlockState(poss25, Blocks.DIRT.getDefaultState());
        BlockPos poss26 = new BlockPos(x2-1, y2+1, z2-2);
        this.world.setBlockState(poss26, Blocks.DIRT.getDefaultState());

        BlockPos poss27 = new BlockPos(x2-2, y2+1, z2);
        this.world.setBlockState(poss27, Blocks.DIRT.getDefaultState());
        BlockPos poss28 = new BlockPos(x2-2, y2+1, z2+1);
        this.world.setBlockState(poss28, Blocks.STONE.getDefaultState());
        BlockPos poss29 = new BlockPos(x2-2, y2+1, z2-1);
        this.world.setBlockState(poss29, Blocks.DIRT.getDefaultState());

        BlockPos poss30 = new BlockPos(x2+1, y2+2, z2);
        this.world.setBlockState(poss30, Blocks.STONE.getDefaultState());
        BlockPos poss31 = new BlockPos(x2+1, y2+2, z2-1);
        this.world.setBlockState(poss31, Blocks.DIRT.getDefaultState());
        BlockPos poss32 = new BlockPos(x2+1, y2+2, z2+1);
        this.world.setBlockState(poss32, Blocks.DIRT.getDefaultState());

        BlockPos poss33 = new BlockPos(x2, y2+2, z2+1);
        this.world.setBlockState(poss33, Blocks.DIRT.getDefaultState());
        BlockPos poss34 = new BlockPos(x2+1, y2+2, z2+1);
        this.world.setBlockState(poss34, Blocks.STONE.getDefaultState());
        BlockPos poss35 = new BlockPos(x2-1, y2+2, z2+1);
        this.world.setBlockState(poss35, Blocks.DIRT.getDefaultState());

        BlockPos poss36 = new BlockPos(x2, y2+2, z2-1);
        this.world.setBlockState(poss36, Blocks.STONE.getDefaultState());
        BlockPos poss37 = new BlockPos(x2+1, y2+2, z2-1);
        this.world.setBlockState(poss37, Blocks.DIRT.getDefaultState());
        BlockPos poss38 = new BlockPos(x2-1, y2+2, z2-1);
        this.world.setBlockState(poss38, Blocks.STONE.getDefaultState());
        BlockPos poss39 = new BlockPos(x2-1, y2+2, z2);
        this.world.setBlockState(poss39, Blocks.DIRT.getDefaultState());
        BlockPos poss40 = new BlockPos(x2, y2+3, z2);
        this.world.setBlockState(poss40, Blocks.STONE.getDefaultState());










        // This loops through the "affectedBlocks" from the explosion
        /*
        for (BlockPos blockPos : this.affectedBlocks) {
            BlockState blockState = this.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if ((block.getClass() == ExplosiveBlock.class) || (block.getClass() == TntBlock.class)) {
                block.onDestroyedByExplosion(this.world, blockPos, null);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState()); // Delete a explosion block
            }

            // randint was 3
            if (this.random.nextInt(6) != 0 || !this.world.getBlockState(blockPos).isAir() || !this.world.getBlockState(blockPos.down()).isOpaqueFullCube(this.world, blockPos.down())) continue;

            //this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
         */
            /*
            for (int x = -6; x < 6; x++) {
                for (int z = -6; z < 6; z++) {
                    if (this.random.nextInt(11) < 6) {
                        if (this.world.getBlockState(blockPos.add(x, -1, z)).isOpaqueFullCube(this.world,
                                blockPos.add(x, -1, z))) {
                            this.world.setBlockState(blockPos.add(x, 0, z),
                                    Blocks.SNOW.getDefaultState());
                        }

                    }
                }
            }
            final int minBaseHeight = 5;
            final int maxBaseHeight = 10;
            int height = 2 * (random.nextInt(maxBaseHeight - minBaseHeight) + minBaseHeight);
            for (int i = 0; i < height / 2; i++) {
                Block icyBlock = this.calculateIcyBlock(height, i);
                this.world.setBlockState(blockPos, icyBlock.getDefaultState());
                icyBlock = this.calculateIcyBlock(height, i);
                this.world.setBlockState(blockPos.east(), icyBlock.getDefaultState());
                icyBlock = this.calculateIcyBlock(height, i);
                this.world.setBlockState(blockPos.north(), icyBlock.getDefaultState());
                icyBlock = this.calculateIcyBlock(height, i);
                this.world.setBlockState(blockPos.west(), icyBlock.getDefaultState());
                icyBlock = this.calculateIcyBlock(height, i);
                this.world.setBlockState(blockPos.south(), icyBlock.getDefaultState());
                blockPos = blockPos.up();
                if (i == ((height / 2) - 1)) {
                    for (int j = height / 2; j < height + random.nextInt(10); j++) {
                        icyBlock = this.calculateIcyBlock(height, j);
                        this.world.setBlockState(blockPos, icyBlock.getDefaultState());
                        blockPos = blockPos.up();
                    }
                }

            }

        }*/
        /*
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
        float q = this.power * 2.0f; // This is the "damage-radius" for entities
        for (int v = 0; v < affectedEntities.size(); ++v) {
            double z;
            double y;
            double x;
            double aa;
            double w;
            Entity entity = affectedEntities.get(v);
            if (entity.isImmuneToExplosion() || !((w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double)q) <= 1.0) || (aa = Math.sqrt((x = entity.getX() - this.x) * x + (y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y) * y + (z = entity.getZ() - this.z) * z)) == 0.0) continue;
            x /= aa;
            y /= aa;
            z /= aa;
            double ab = EarthExplosion.getExposure(vec3d, entity);
            double ac = (1.0 - w) * ab;

            double ad = ac;
            if (entity instanceof LivingEntity) {
                ad = ProtectionEnchantment.transformExplosionKnockback((LivingEntity)entity, ac);
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 1,
                        false, true, true));
            }

            entity.setVelocity(entity.getVelocity().add(x * ad, y * ad, z * ad));

        }*/
    }

    Block calculateIcyBlock(int maxHeight, int currentHeight) {
        float percentage = ((float)currentHeight / (float)maxHeight);
        System.out.println(percentage);
        if (percentage < 0.3 + (float)random.nextInt(15) / 100) {
            if (random.nextInt(10) < 7) {
                return Blocks.PACKED_ICE;
            } else if (random.nextInt(10) < 7) {
                return Blocks.SNOW_BLOCK;
            } else return Blocks.ICE;
        } else if (percentage < 0.5 + (float)random.nextInt(15) / 100) {
            if (random.nextInt(2) == 0) {
                return Blocks.ICE;
            } else return Blocks.SNOW_BLOCK;
        } else if (percentage < 0.7){
            if (random.nextInt(10) < 7) {
                return Blocks.ICE;
            } else return Blocks.SNOW_BLOCK;
        } else return Blocks.ICE;
    }
}
