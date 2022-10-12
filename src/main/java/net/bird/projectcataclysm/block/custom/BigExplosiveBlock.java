package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.entity.custom.BigExplosiveEntity;

public class BigExplosiveBlock extends ExplosiveBlock {
    public BigExplosiveBlock(Settings settings) {
        super(settings);
        this.explosiveEntity = BigExplosiveEntity.class;
    }
}
