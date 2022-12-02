package net.bird.projectcataclysm.event;

import net.bird.projectcataclysm.item.custom.PortalGunItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_PORTAL = "key.category.projectcataclysm.portal";
    public static final String KEY_BLUE_PORTAL = "key.projectcataclysm.blue_portal";
    public static final String KEY_ORANGE_PORTAL = "key.projectcataclysm.orange_portal";

    public static final String KEY_CLEAR_PORTALS = "key.projectcataclysm.clear_portals";



    public static KeyBinding bluePortalKey;
    public static KeyBinding orangePortalKey;

    public static KeyBinding clearPortalsKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (bluePortalKey.wasPressed()) {
                PortalGunItem.setCurrentPortal(PortalGunItem.BLUE);
                PortalGunItem.sendUpdate("Blue Portal");
            }

            if (orangePortalKey.wasPressed()) {
                PortalGunItem.setCurrentPortal(PortalGunItem.ORANGE);
                PortalGunItem.sendUpdate("Orange Portal");
            }

            if (clearPortalsKey.wasPressed()) {

                PortalGunItem.clearPortals();
                PortalGunItem.sendUpdate("Cleared Portals");

            }
        });
    }


    public static void register() {
        bluePortalKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_BLUE_PORTAL,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                KEY_CATEGORY_PORTAL
        ));

        orangePortalKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ORANGE_PORTAL,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEY_CATEGORY_PORTAL
        ));

        clearPortalsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_CLEAR_PORTALS,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KEY_CATEGORY_PORTAL
        ));
        registerKeyInputs();
    }


}
