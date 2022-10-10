package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.custom.FireExplosiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FireExplosiveBlock extends ExplosiveBlock {
    public FireExplosiveBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void primeExplosive(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        FireExplosiveEntity explosiveEntity = new FireExplosiveEntity(world, (double)pos.getX() + 0.5, pos.getY(),
                (double)pos.getZ() + 0.5, igniter);
        world.spawnEntity(explosiveEntity);

        // Play sound event
        world.playSound(null, explosiveEntity.getX(), explosiveEntity.getY(), explosiveEntity.getZ(),
                SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);

        // Create game event
        world.emitGameEvent((Entity)igniter, GameEvent.PRIME_FUSE, pos);
    }

    // TODO fix explosion activated ignition causing tnt entity to spawn
    // TODO add crafting recipe and block drop
}
