package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.explosive.LargeTntEntity;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class LargeTntBlock extends TntBlock {
    public LargeTntBlock(Settings settings) {
        super(settings);
    }

    private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!world.isClient) {

            LargeTntEntity explosiveEntity = new LargeTntEntity(world, (double)pos.getX() + 0.5, (double)pos.getY(),
                    (double)pos.getZ() + 0.5, igniter);

            world.spawnEntity(explosiveEntity);
            world.playSound((PlayerEntity)null, explosiveEntity.getX(), explosiveEntity.getY(), explosiveEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }
}
