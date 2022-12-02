package net.bird.projectcataclysm;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.entity.ModEntities;
import net.bird.projectcataclysm.entity.custom.*;
import net.bird.projectcataclysm.event.KeyInputHandler;
import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.screen.ControlPanelScreen;
import net.bird.projectcataclysm.screen.RemoteControlScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.bird.projectcataclysm.screen.FabricatingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ProjectCataclysmModClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_MISSILE_LAYER = new EntityModelLayer(new Identifier(ProjectCataclysmMod.MOD_ID, "missile"), "main");

    public static final Identifier SEND_POS_PACKET_ID = new Identifier(ProjectCataclysmMod.MOD_ID, "send_pos");
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

        EntityRendererRegistry.register(ModEntities.WATER_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.WATER_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.NATURE_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.NATURE_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.CLUSTER_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.CLUSTER_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.POISON_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.POISON_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.EARTH_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.EARTH_EXPLOSIVE));

        EntityRendererRegistry.register(ModEntities.SUCTION_EXPLOSIVE,
                (a) -> new ExplosiveRenderer(a, ModBlocks.SUCTION_EXPLOSIVE));

        // --------------------------

        EntityRendererRegistry.register(ModEntities.BulletEntityType, BulletEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SlugEntityType, SlugEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.ExplosiveProjectileEntityType, ExplosiveProjectileEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.PortalProjectileEntityType, PortalProjectileRenderer::new);


        HandledScreens.register(ProjectCataclysmMod.FABRICATING_HANDLER, FabricatingScreen::new);
        HandledScreens.register(ProjectCataclysmMod.CONTROL_PANEL_HANDLER, ControlPanelScreen::new);
        HandledScreens.register(ProjectCataclysmMod.REMOTE_CONTROL_HANDLER, RemoteControlScreen::new);
        EntityRendererRegistry.register(ModEntities.MISSILE, MissileEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_MISSILE_LAYER, MissileEntityModel::getTexturedModelData);
        ModelPredicateProviderRegistry.register(ModItems.SILVER_SHIELD, new Identifier("blocking"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
        ModelPredicateProviderRegistry.register(ModItems.PROTECTIVE_BARRIER, new Identifier("deployed"), (itemStack, clientWorld, livingEntity, i) -> itemStack.getOrCreateNbt().contains("DeployedX") ? 1 : 0);
        ClientPlayNetworking.registerGlobalReceiver(SEND_POS_PACKET_ID, ((client, handler, buf, responseSender) -> {
            List<BlockPos> listPos = buf.readList(PacketByteBuf::readBlockPos);
            client.execute(() -> {
                if (client.currentScreen instanceof RemoteControlScreen) {
                    ((RemoteControlScreen) client.currentScreen).posList = listPos;
                }
                if (client.currentScreen instanceof ControlPanelScreen) {
                    ((ControlPanelScreen) client.currentScreen).posList = listPos;
                }
            });
        }));


        KeyInputHandler.register();
    }
}