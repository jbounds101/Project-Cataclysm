package net.bird.projectcataclysm;

import net.bird.projectcataclysm.screen.FabricatingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class ProjectCataclysmModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ProjectCataclysmMod.FABRICATING_HANDLER, FabricatingScreen::new);
    }
}
