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

public abstract class ExplosiveBlock extends TntBlock {

   ExplosiveBlockEntity explosiveBlockEntity;

    public ExplosiveBlock(Settings settings, ExplosiveBlockEntity explosiveBlockEntity) {
        super(settings);
        this.explosiveBlockEntity = explosiveBlockEntity;
    }

    private void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {

        if (!world.isClient) {
            /*ExplosiveBlockEntity explosiveEntity = new explosiveBlockEntity(world,
                    (double)pos.getX() + 0.5,
                    (double)pos.getY(),
                    (double)pos.getZ() + 0.5, igniter); I might need this later, probably not though*/

            world.spawnEntity(explosiveBlockEntity);
            world.playSound((PlayerEntity)null, explosiveBlockEntity.getX(), explosiveBlockEntity.getY(),
                    explosiveBlockEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }
}
