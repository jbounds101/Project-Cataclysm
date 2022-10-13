package net.bird.projectcataclysm.entity;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.entity.custom.BigExplosiveEntity;
import net.bird.projectcataclysm.entity.custom.ExplosiveEntity;
import net.bird.projectcataclysm.entity.custom.FireExplosiveEntity;
import net.bird.projectcataclysm.entity.custom.MassiveExplosiveEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unchecked")
public class ModEntities implements ModInitializer {

    // --- Explosives ---
    public static final EntityType<BigExplosiveEntity> BIG_EXPLOSIVE =
            (EntityType<BigExplosiveEntity>) registerExplosive("big_explosive_entity", BigExplosiveEntity::new);

    public static final EntityType<MassiveExplosiveEntity> MASSIVE_EXPLOSIVE =
            (EntityType<MassiveExplosiveEntity>) registerExplosive(
                    "massive_explosive_entity", MassiveExplosiveEntity::new);

    public static final EntityType<FireExplosiveEntity> FIRE_EXPLOSIVE =
            (EntityType<FireExplosiveEntity>) registerExplosive(
                    "fire_explosive_entity", FireExplosiveEntity::new);

    private static EntityType<? extends ExplosiveEntity> registerExplosive(String name,
                                                   EntityType.EntityFactory<? extends ExplosiveEntity> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(ProjectCataclysmMod.MOD_ID, name),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory)
                .dimensions(new EntityDimensions(0.98F, 0.98F, false)).fireImmune()
                .trackRangeBlocks(10).trackedUpdateRate(10).build());
    }
    // ------------------

    @Override
    public void onInitialize() {
    }
}