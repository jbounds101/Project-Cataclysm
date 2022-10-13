package net.bird.projectcataclysm;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.entity.custom.BulletEntity;
import net.bird.projectcataclysm.entity.custom.BulletEntityRenderer;
import net.bird.projectcataclysm.entity.custom.ExplosiveRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class ProjectCataclysmModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

       // --- Explosive Rendering ---
        EntityRendererRegistry.register(ModEntities.BIG_EXPLOSIVE,
                (a)-> new ExplosiveRenderer(a, ModBlocks.BIG_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.MASSIVE_EXPLOSIVE,
                (a)-> new ExplosiveRenderer(a, ModBlocks.MASSIVE_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.FIRE_EXPLOSIVE,
                (a)-> new ExplosiveRenderer(a, ModBlocks.FIRE_EXPLOSIVE));

        // --------------------------

        EntityRendererRegistry.register(ModEntities.BulletEntityType,
                (context) -> new BulletEntityRenderer(context));

    }
}
