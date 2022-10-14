package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.custom.FireExplosiveEntity;

public class FireExplosiveBlock extends ExplosiveBlock {
    public FireExplosiveBlock(Settings settings) {
        super(settings);
        this.explosiveEntity = FireExplosiveEntity.class;
    }
}
