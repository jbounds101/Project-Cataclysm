package net.bird.projectcataclysm.entity;

import net.bird.projectcataclysm.block.custom.BigExplosiveBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.registry.Registry;

public class ModEntities implements ModInitializer {
    public static final EntityType<BigExplosiveBlock> BIG_EXPLOSIVE = Registry.register("tnt",
            EntityType.Builder.create(TntEntity::new,
                                                                  SpawnGroup.MISC).makeFireImmune().setDimensions(0.98F, 0.98F).maxTrackingRange(10).trackingTickInterval(10));
}
