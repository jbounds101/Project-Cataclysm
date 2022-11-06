package net.bird.projectcataclysm.block;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.custom.*;
import net.bird.projectcataclysm.entity.custom.*;
import net.bird.projectcataclysm.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.ToIntFunction;


public class ModBlocks {
    private static ToIntFunction<BlockState> getLaunchPlatformLuminance() {
        return (state) -> state.get(LaunchPlatformBlock.TYPE) == LaunchPlatformType.CONTROL_PANEL ? 15 : 5;
    }

    /* Every block needs (in assets folder):
       1. en_us.json translation
       2. block model json
       3. block texture png
       4. item model json (can be simple and point to block model json)
       5. blockstates json
       6. if block requires tool
            6a. update data.minecraft.tags.blocks.mineable.(tool)
            6b. update data.minecraft.tags.blocks.mineable.(needs stone/iron/diamond tool)
    */

    // --- Silver ---
    public static final Block SILVER_BLOCK = registerBlock("silver_block",
            new Block(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).hardness(3f).resistance(6f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block SILVER_ORE = registerBlock("silver_ore",
            new Block(FabricBlockSettings.of(Material.STONE).strength(3f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
            new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.DEEPSLATE).hardness(4.5f).resistance(3f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block END_SILVER_ORE = registerBlock("end_silver_ore",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(4.5f).resistance(6f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block RAW_SILVER_BLOCK = registerBlock("raw_silver_block",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(5f).resistance(6f).requiresTool()),
            ModItemGroup.PROJECT_CATACLYSM);
    // --------------

    // --- Explosives ---
    public static final Block BIG_EXPLOSIVE = registerBlock("big_explosive",
            new ExplosiveBlock(FabricBlockSettings.of(Material.TNT).sounds(BlockSoundGroup.GRASS),
                    BigExplosiveEntity.class), ModItemGroup.PROJECT_CATACLYSM);

    public static final Block MASSIVE_EXPLOSIVE = registerBlock("massive_explosive",
            new ExplosiveBlock(FabricBlockSettings.of(Material.TNT).sounds(BlockSoundGroup.GRASS),
                    MassiveExplosiveEntity.class), ModItemGroup.PROJECT_CATACLYSM);

    public static final Block FIRE_EXPLOSIVE = registerBlock("fire_explosive",
            new ExplosiveBlock(FabricBlockSettings.of(Material.TNT).sounds(BlockSoundGroup.GRASS),
                    FireExplosiveEntity.class), ModItemGroup.PROJECT_CATACLYSM);

    public static final Block LIGHTNING_EXPLOSIVE = registerBlock("lightning_explosive",
            new ExplosiveBlock(FabricBlockSettings.of(Material.TNT).sounds(BlockSoundGroup.GRASS),
                    LightningExplosiveEntity.class), ModItemGroup.PROJECT_CATACLYSM);

    public static final Block ICE_EXPLOSIVE = registerBlock("ice_explosive",
            new ExplosiveBlock(FabricBlockSettings.of(Material.TNT).sounds(BlockSoundGroup.GRASS),
                    IceExplosiveEntity.class), ModItemGroup.PROJECT_CATACLYSM);

    public static final Block AIR_EXPLOSIVE = registerBlock("air_explosive",
            new ExplosiveBlock(FabricBlockSettings.of(Material.TNT).sounds(BlockSoundGroup.GRASS),
                    AirExplosiveEntity.class), ModItemGroup.PROJECT_CATACLYSM);
    // ------------------

    public static final Block SOUL_ESSENCE = registerBlock("soul_essence",
            new Block(FabricBlockSettings.of(Material.AGGREGATE).breakInstantly().luminance(13).noCollision().nonOpaque().sounds(BlockSoundGroup.SCULK_SHRIEKER)),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block FABRICATOR = registerBlock("fabricator",
            new FabricatorBlock(FabricBlockSettings.of(Material.METAL).luminance(10).hardness(50f).resistance(12000f).requiresTool().sounds(BlockSoundGroup.METAL)),
            ModItemGroup.PROJECT_CATACLYSM);

    public static final Block PROTECTIVE_BARRIER_BLOCK = Registry.register(Registry.BLOCK, new Identifier(ProjectCataclysmMod.MOD_ID, "protective_barrier_block"),
            new Block(FabricBlockSettings.of(Material.STONE).luminance(15).hardness(-1.0f).resistance(3600000.0f).dropsNothing()));

    public static final Block LAUNCH_PLATFORM = Registry.register(Registry.BLOCK, new Identifier(ProjectCataclysmMod.MOD_ID, "launch_platform"),
            new LaunchPlatformBlock(FabricBlockSettings.of(Material.METAL).luminance(getLaunchPlatformLuminance()).hardness(-1.0f).resistance(12000f).sounds(BlockSoundGroup.ANVIL)));

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
