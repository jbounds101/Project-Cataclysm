package net.bird.projectcataclysm;

import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.entity.custom.BigExplosiveRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ProjectCataclysmModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.BIG_EXPLOSIVE, BigExplosiveRenderer::new);


    }
}
