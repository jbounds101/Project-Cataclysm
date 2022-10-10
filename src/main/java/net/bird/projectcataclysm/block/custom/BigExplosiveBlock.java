package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.entity.custom.BigExplosiveEntity;
import net.minecraft.entity.Entity;
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

public class BigExplosiveBlock extends ExplosiveBlock {
    public BigExplosiveBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void primeExplosive(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!world.isClient) {
            BigExplosiveEntity explosiveEntity = new BigExplosiveEntity(world, pos.getX() + 0.5,
                    pos.getY(), pos.getZ() + 0.5, igniter);
            world.spawnEntity(explosiveEntity);
            world.playSound(null, explosiveEntity.getX(), explosiveEntity.getY(), explosiveEntity.getZ(),
                    SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient) {
            BigExplosiveEntity explosiveEntity = new BigExplosiveEntity(world, pos.getX() + 0.5,
                    pos.getY(), pos.getZ() + 0.5, explosion.getCausingEntity());
            int i = explosiveEntity.getFuse();
            explosiveEntity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
            world.spawnEntity(explosiveEntity);
        }
    }

}
