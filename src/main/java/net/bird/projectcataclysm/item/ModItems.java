package net.bird.projectcataclysm.item;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
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
    public static final Item SOUL_SHARD = registerItem("soul_shard",
            new Item(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));
    public static final Item LIFE_SWAP = registerItem("life_swap",
            new Item(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM).maxCount(1)));

    public static final Item SOUL_GEM = registerItem("soul_gem",
            new SoulGemItem(new FabricItemSettings().rarity(Rarity.RARE).group(ModItemGroup.PROJECT_CATACLYSM)));
    public static final ToolItem SILVER_SCYTHE = new ScytheItem(SilverToolMaterial.INSTANCE, new FabricItemSettings().rarity(Rarity.UNCOMMON).fireproof().group(ModItemGroup.PROJECT_CATACLYSM));
    public static final Item SILVER_SHIELD = registerItem("silver_shield",
            new Item(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    public static final Item PISTOL = registerItem("pistol",
            new PistolItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM).maxCount(1)));

    public static final Item SNIPER = registerItem("sniper",
            new SniperItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM).maxCount(1)));

    public static final Item BULLET = registerItem("bullet",
            new BulletItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    public static final Item DOWSING_ROD = registerItem("dowsing_rod",
            new DowsingRodItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    public static final Item WAND_LIFE_SWAP = registerItem("wand_life_swap",
            new WandLifeSwap(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM).maxCount(1)));

    public static final Item MACHINE_GUN = registerItem("machine_gun",
            new MachineGunItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM).maxDamage(16)));

    public static final Item WAND = registerItem("wand",
            new WandItem(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM), 2));

    public static final Item WALL_WAND = registerItem("wall_wand",
            new WallWand(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    public static final Item WEB_WAND = registerItem("web_wand",
            new WebWand(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    public static final Item BLINK_WAND = registerItem("blink_wand",
            new BlinkWand(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    public static final Item MAGIC_WAND = registerItem("magic_wand",
            new MagicWand(new FabricItemSettings().group(ModItemGroup.PROJECT_CATACLYSM)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ProjectCataclysmMod.MOD_ID, name), item);
        /* Identifier acts as a namespace:object pair, for example, minecraft:iron_ingot, or in this case,
           projectcataclysm:"name" */
    }

    public static void registerModItems() {
        ProjectCataclysmMod.LOGGER.info("Registering ModItems for " + ProjectCataclysmMod.MOD_ID);
    }
}
