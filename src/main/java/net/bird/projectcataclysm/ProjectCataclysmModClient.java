package net.bird.projectcataclysm;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.entity.custom.BulletEntityRenderer;
import net.bird.projectcataclysm.entity.custom.ExplosiveRenderer;
import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.screen.ControlPanelScreen;
import net.bird.projectcataclysm.entity.custom.MissileEntityModel;
import net.bird.projectcataclysm.entity.custom.MissileEntityRenderer;
import net.bird.projectcataclysm.entity.custom.SlugEntityRenderer;
import net.bird.projectcataclysm.screen.RemoteControlScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.bird.projectcataclysm.screen.FabricatingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ProjectCataclysmModClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_MISSILE_LAYER = new EntityModelLayer(new Identifier(ProjectCataclysmMod.MOD_ID, "missile"), "main");
    @Override
    public void onInitializeClient() {

        // --- Explosive Rendering ---
        EntityRendererRegistry.register(ModEntities.BIG_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.BIG_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.MASSIVE_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.MASSIVE_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.FIRE_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.FIRE_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.LIGHTNING_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.LIGHTNING_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.ICE_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.ICE_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.AIR_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.AIR_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.FLASH_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.FLASH_EXPLOSIVE));

        // --------------------------

        EntityRendererRegistry.register(ModEntities.BulletEntityType,
                (context) -> new BulletEntityRenderer(context));
        EntityRendererRegistry.register(ModEntities.SlugEntityType,
                SlugEntityRenderer::new);
        HandledScreens.register(ProjectCataclysmMod.FABRICATING_HANDLER, FabricatingScreen::new);
        HandledScreens.register(ProjectCataclysmMod.CONTROL_PANEL_HANDLER, ControlPanelScreen::new);
        HandledScreens.register(ProjectCataclysmMod.REMOTE_CONTROL_HANDLER, RemoteControlScreen::new);
        EntityRendererRegistry.register(ModEntities.MISSILE, MissileEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_MISSILE_LAYER, MissileEntityModel::getTexturedModelData);
        ModelPredicateProviderRegistry.register(ModItems.SILVER_SHIELD, new Identifier("blocking"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
        ModelPredicateProviderRegistry.register(ModItems.PROTECTIVE_BARRIER, new Identifier("deployed"), (itemStack, clientWorld, livingEntity, i) -> itemStack.getOrCreateNbt().contains("DeployedX") ? 1 : 0);
    }
}