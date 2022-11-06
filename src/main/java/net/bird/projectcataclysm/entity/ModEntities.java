package net.bird.projectcataclysm.entity;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.entity.custom.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unchecked")
public class ModEntities implements ModInitializer {

    private static final int explosiveRenderRange = 10;

    // --- Explosives ---
    public static final EntityType<BigExplosiveEntity> BIG_EXPLOSIVE =
            (EntityType<BigExplosiveEntity>) registerExplosive(
                    "big_explosive_entity", BigExplosiveEntity::new);

    public static final EntityType<MassiveExplosiveEntity> MASSIVE_EXPLOSIVE =
            (EntityType<MassiveExplosiveEntity>) registerExplosive(
                    "massive_explosive_entity", MassiveExplosiveEntity::new);

    public static final EntityType<FireExplosiveEntity> FIRE_EXPLOSIVE =
            (EntityType<FireExplosiveEntity>) registerExplosive(
                    "fire_explosive_entity", FireExplosiveEntity::new);

    public static final EntityType<LightningExplosiveEntity> LIGHTNING_EXPLOSIVE =
            (EntityType<LightningExplosiveEntity>) registerExplosive(
                    "lightning_explosive_entity", LightningExplosiveEntity::new);

    public static final EntityType<IceExplosiveEntity> ICE_EXPLOSIVE =
            (EntityType<IceExplosiveEntity>) registerExplosive(
                    "ice_explosive_entity", IceExplosiveEntity::new);

    public static final EntityType<AirExplosiveEntity> AIR_EXPLOSIVE =
            (EntityType<AirExplosiveEntity>) registerExplosive(
                    "air_explosive_entity", AirExplosiveEntity::new);
    public static final EntityType<FlashExplosiveEntity> FLASH_EXPLOSIVE =
            (EntityType<FlashExplosiveEntity>) registerExplosive(
                    "flash_explosive_entity", FlashExplosiveEntity::new);


    private static EntityType<? extends ExplosiveEntity> registerExplosive(String name,
                                                   EntityType.EntityFactory<? extends ExplosiveEntity> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(ProjectCataclysmMod.MOD_ID, name),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory)
                .dimensions(new EntityDimensions(0.98F, 0.98F, false)).fireImmune()
                .trackRangeBlocks(explosiveRenderRange).trackedUpdateRate(10).build());
    }
    // ------------------

    public static final EntityType<BulletEntity> BulletEntityType = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(ProjectCataclysmMod.MOD_ID, "bullet"),
            FabricEntityTypeBuilder.<BulletEntity>create(SpawnGroup.MISC, BulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(100).trackedUpdateRate(10).build());

    public static final EntityType<MissileEntity> MISSILE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ProjectCataclysmMod.MOD_ID, "missile"),
            FabricEntityTypeBuilder.<MissileEntity>create(SpawnGroup.MISC, MissileEntity::new).dimensions(EntityDimensions.fixed(1,3)).build()
    );
    @Override
    public void onInitialize() {
    }
}
