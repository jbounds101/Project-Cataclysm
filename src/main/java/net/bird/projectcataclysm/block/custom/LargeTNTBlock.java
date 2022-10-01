package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.CustomExplosion;
import net.bird.projectcataclysm.entity.explosive.ExplosiveBlockEntity;
import net.bird.projectcataclysm.entity.explosive.LargeTNTEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class LargeTNTBlock extends ExplosiveBlock {

    public LargeTNTBlock(Settings settings) throws NoSuchMethodException {
        super(settings, LargeTNTEntity.class, CustomExplosion.class);
    }
}
