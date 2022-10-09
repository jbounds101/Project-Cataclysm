package net.bird.projectcataclysm.entity;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.entity.custom.BigExplosiveEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities implements ModInitializer {
    public static final EntityType<BigExplosiveEntity> BIG_EXPLOSIVE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(ProjectCataclysmMod.MOD_ID, "big_explosive_entity"),
            FabricEntityTypeBuilder.<BigExplosiveEntity>create(SpawnGroup.MISC, BigExplosiveEntity::new)
            .dimensions(new EntityDimensions(0.98F, 0.98F, false)).fireImmune()
            .trackRangeBlocks(10).trackedUpdateRate(10).build());



    @Override
    public void onInitialize() {
    }
}
