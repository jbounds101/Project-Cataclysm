package net.bird.projectcataclysm.item;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.item.custom.DowsingRodItem;
import net.bird.projectcataclysm.item.custom.PistolItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    /* Every item needs (in assets folder):
       1. en_us.json translation
       2. item model json
       3. item texture png
    */
    public static final Item SILVER_INGOT = registerItem("silver_ingot",
            new Item(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));
    public static final Item SILVER_NUGGET = registerItem("silver_nugget",
            new Item(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));
    public static final Item RAW_SILVER = registerItem("raw_silver",
            new Item(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));


    public static final Item DOWSING_ROD = registerItem("dowsing_rod",
            new DowsingRodItem(new FabricItemSettings().group(ItemGroup.MISC)));

    public static final Item PISTOL = registerItem("pistol",
            new PistolItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ProjectCataclysmMod.MOD_ID, name), item);
        /* Identifier acts as a namespace:object pair, for example, minecraft:iron_ingot, or in this case,
           projectcataclysm:"name" */
    }

    public static void registerModItems() {
        ProjectCataclysmMod.LOGGER.info("Registering ModItems for " + ProjectCataclysmMod.MOD_ID);
    }
}
