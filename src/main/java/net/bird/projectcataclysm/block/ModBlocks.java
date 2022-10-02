package net.bird.projectcataclysm.block;

import net.bird.projectcataclysm.CustomExplosion;
import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.custom.LargeTNTBlock;
import net.bird.projectcataclysm.entity.explosive.LargeTNTEntity;
import net.bird.projectcataclysm.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {


    /* Every block needs (in assets folder):
       1. en_us.json translation
       2. block model json
       3. block texture png
       4. item model json (can be simple and point to block model json)
       5. if block requires tool
            5a. update data.minecraft.tags.blocks.mineable.(tool)
            5b. update data.minecraft.tags.blocks.mineable.(needs stone/iron/diamond tool)
    */
    public static final Block SILVER_BLOCK = registerBlock("silver_block",
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).hardness(3f).resistance(6f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block SILVER_ORE = registerBlock("silver_ore",
            new Block(FabricBlockSettings.of(Material.STONE).strength(3f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.DEEPSLATE).hardness(4.5f).resistance(3f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block RAW_SILVER_BLOCK = registerBlock("raw_silver_block",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(5f).resistance(6f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block LARGE_TNT;
    static {
        try {
            LARGE_TNT = registerBlock("large_tnt",
                    new LargeTNTBlock(FabricBlockSettings.of(Material.TNT)),
                    ModItemGroup.PROJECT_CATACLYSM);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(ProjectCataclysmMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(ProjectCataclysmMod.MOD_ID, name),
            new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks() {
        ProjectCataclysmMod.LOGGER.info("Registering ModBlocks for " + ProjectCataclysmMod.MOD_ID);
    }
}
