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

    public static final EntityType<WaterExplosiveEntity> WATER_EXPLOSIVE =
            (EntityType<WaterExplosiveEntity>) registerExplosive(
                    "water_explosive_entity", WaterExplosiveEntity::new);

    public static final EntityType<NatureExplosiveEntity> NATURE_EXPLOSIVE =
            (EntityType<NatureExplosiveEntity>) registerExplosive(
                    "nature_explosive_entity", NatureExplosiveEntity::new);

    public static final EntityType<ClusterExplosiveEntity> CLUSTER_EXPLOSIVE =
            (EntityType<ClusterExplosiveEntity>) registerExplosive(
                    "cluster_explosive_entity", ClusterExplosiveEntity::new);

    public static final EntityType<PoisonExplosiveEntity> POISON_EXPLOSIVE =
            (EntityType<PoisonExplosiveEntity>) registerExplosive(
                    "poison_explosive_entity", PoisonExplosiveEntity::new);

    public static final EntityType<EarthExplosiveEntity> EARTH_EXPLOSIVE =
            (EntityType<EarthExplosiveEntity>) registerExplosive(
                    "earth_explosive_entity", EarthExplosiveEntity::new);

    public static final EntityType<SuctionExplosiveEntity> SUCTION_EXPLOSIVE =
            (EntityType<SuctionExplosiveEntity>) registerExplosive(
                    "suction_explosive_entity", SuctionExplosiveEntity::new);

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

    public static final EntityType<SlugEntity> SlugEntityType = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(ProjectCataclysmMod.MOD_ID, "slug"),
            FabricEntityTypeBuilder.<SlugEntity>create(SpawnGroup.MISC, SlugEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(100).trackedUpdateRate(10).build());

    public static final EntityType<ExplosiveProjectileEntity> ExplosiveProjectileEntityType = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(ProjectCataclysmMod.MOD_ID, "explosive_projectile"),
            FabricEntityTypeBuilder.<ExplosiveProjectileEntity>create(SpawnGroup.MISC, ExplosiveProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(100).trackedUpdateRate(10).build());

    public static final EntityType<PortalProjectileEntity> PortalProjectileEntityType = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(ProjectCataclysmMod.MOD_ID, "portal_projectile"),
            FabricEntityTypeBuilder.<PortalProjectileEntity>create(SpawnGroup.MISC, PortalProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(100).trackedUpdateRate(10).build());


    @Override
    public void onInitialize() {
    }
}
