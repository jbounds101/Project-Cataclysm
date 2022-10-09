package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.custom.BigExplosiveEntity;
import net.bird.projectcataclysm.entity.custom.ExplosiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BigExplosiveBlock extends ExplosiveBlock {
    public BigExplosiveBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void primeExplosive(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        ExplosiveEntity explosiveEntity = new BigExplosiveEntity(world, (double)pos.getX() + 0.5, pos.getY(),
                (double)pos.getZ() + 0.5, igniter);
        world.spawnEntity(explosiveEntity);

        // Play sound event
        world.playSound(null, explosiveEntity.getX(), explosiveEntity.getY(), explosiveEntity.getZ(),
                SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);

        // Create game event
        world.emitGameEvent((Entity)igniter, GameEvent.PRIME_FUSE, pos);
    }

}
