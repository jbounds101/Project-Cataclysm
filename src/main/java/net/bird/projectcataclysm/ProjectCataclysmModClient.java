package net.bird.projectcataclysm;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.entity.custom.BulletEntityRenderer;
import net.bird.projectcataclysm.entity.custom.ExplosiveRenderer;
import net.bird.projectcataclysm.entity.custom.SlugEntityRenderer;
import net.bird.projectcataclysm.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.bird.projectcataclysm.screen.FabricatingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ProjectCataclysmModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // --- Explosive Rendering ---
        EntityRendererRegistry.register(ModEntities.BIG_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.BIG_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.MASSIVE_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.MASSIVE_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.FIRE_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.FIRE_EXPLOSIVE));

        // --------------------------

        EntityRendererRegistry.register(ModEntities.BulletEntityType,
                BulletEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SlugEntityType,
                SlugEntityRenderer::new);
        HandledScreens.register(ProjectCataclysmMod.FABRICATING_HANDLER, FabricatingScreen::new);
        ModelPredicateProviderRegistry.register(ModItems.SILVER_SHIELD, new Identifier("blocking"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
        ModelPredicateProviderRegistry.register(ModItems.PROTECTIVE_BARRIER, new Identifier("deployed"), (itemStack, clientWorld, livingEntity, i) -> itemStack.getOrCreateNbt().contains("DeployedX") ? 1 : 0);
    }
}