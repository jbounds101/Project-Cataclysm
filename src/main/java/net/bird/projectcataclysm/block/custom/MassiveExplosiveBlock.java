package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.custom.MassiveExplosiveEntity;

public class MassiveExplosiveBlock extends ExplosiveBlock {
    public MassiveExplosiveBlock(Settings settings) {
        super(settings);
        explosiveEntity = MassiveExplosiveEntity.class;
    }
}
