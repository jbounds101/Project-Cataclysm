package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.CustomExplosion;
import net.bird.projectcataclysm.entity.explosive.ExplosiveBlockEntity;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ExplosiveBlock extends TntBlock {

   private final Constructor<? extends ExplosiveBlockEntity> explosiveBlockEntityConstructor;
   private final Class<? extends CustomExplosion> customExplosion;
   private int fuse = 80;

    public ExplosiveBlock(Settings settings, Class<? extends ExplosiveBlockEntity> explosiveBlockEntity,
                          Class<? extends CustomExplosion> customExplosion) throws NoSuchMethodException {
        super(settings);
        //World world, double x, double y, double z, @Nullable LivingEntity igniter, int fuse,
        //                           Class<CustomExplosion> customExplosion
        /*Class[] parameters = new Class[] { World.class, Double.class, Double.class, Double.class, LivingEntity.class,
            Integer.class, Class.class};*/
        this.explosiveBlockEntityConstructor = explosiveBlockEntity.getConstructor(World.class, Double.class,
                Double.class, Double.class, LivingEntity.class, Integer.class, CustomExplosion.class);
        this.customExplosion = customExplosion;
    }

    private void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        if (!world.isClient) {
            /*ExplosiveBlockEntity explosiveEntity = new explosiveBlockEntity(world,
                    (double)pos.getX() + 0.5,
                    (double)pos.getY(),
                    (double)pos.getZ() + 0.5, igniter); I might need this later, probably not though*/

            //World world, double x, double y, double z, @Nullable LivingEntity igniter, int fuse,
            //                           CustomExplosion customExplosion
            ExplosiveBlockEntity explosiveBlockEntity =
                    explosiveBlockEntityConstructor.newInstance(world, (double)pos.getX(), (double)pos.getY(),
                            (double)pos.getZ(), igniter, fuse, customExplosion);

            world.spawnEntity(explosiveBlockEntity);
            world.playSound((PlayerEntity)null, explosiveBlockEntity.getX(), explosiveBlockEntity.getY(),
                    explosiveBlockEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }
}
