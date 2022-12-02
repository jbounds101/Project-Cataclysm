package net.bird.projectcataclysm;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.block.custom.ExplosiveBlock;
import net.bird.projectcataclysm.block.custom.LaunchPlatformBlock;
import net.bird.projectcataclysm.block.custom.LaunchPlatformBlockEntity;
import net.bird.projectcataclysm.entity.custom.MissileEntity;
import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.recipe.FabricatingRecipe;
import net.bird.projectcataclysm.screen.ControlPanelScreenHandler;
import net.bird.projectcataclysm.screen.FabricatingScreenHandler;
import net.bird.projectcataclysm.screen.RemoteControlScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

public class ProjectCataclysmMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static final String MOD_ID = "projectcataclysm";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final ConfiguredFeature<?, ?> END_SILVER_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>
			(Feature.ORE, new OreFeatureConfig(
					new BlockMatchRuleTest(Blocks.END_STONE),
					ModBlocks.END_SILVER_ORE.getDefaultState(),
					4));

	public static PlacedFeature END_SILVER_ORE_PLACED_FEATURE = new PlacedFeature(
			RegistryEntry.of(END_SILVER_ORE_CONFIGURED_FEATURE),
			Arrays.asList(
					CountPlacementModifier.of(10),
					SquarePlacementModifier.of(),
					HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))));
	public static ModelIdentifier SILVER_SCYTHE_INVENTORY = new ModelIdentifier(MOD_ID,"silver_scythe_inventory","inventory");
	public static RecipeSerializer<FabricatingRecipe> FABRICATING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "fabricating"), new FabricatingRecipe.Serializer());

	public static RecipeType<FabricatingRecipe> FABRICATING = Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, "fabricating"), new RecipeType<FabricatingRecipe>() {
		public String toString() {
			return "fabricating";
		}
	});
	public static final ScreenHandlerType<FabricatingScreenHandler> FABRICATING_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(MOD_ID, "fabricating"), new ScreenHandlerType<>(FabricatingScreenHandler::new));

	public static final ExtendedScreenHandlerType<ControlPanelScreenHandler> CONTROL_PANEL_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(MOD_ID, "control_panel"), new ExtendedScreenHandlerType<>(ControlPanelScreenHandler::new));

	public static final ExtendedScreenHandlerType<RemoteControlScreenHandler> REMOTE_CONTROL_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(MOD_ID, "remote_control"), new ExtendedScreenHandlerType<>(RemoteControlScreenHandler::new));
	public static final TagKey<Item> MISSILE_PAYLOADS = TagKey.of(Registry.ITEM_KEY, new Identifier(ProjectCataclysmMod.MOD_ID, "missile_payloads"));
	public static final Identifier DISMANTLE_PACKET_ID = new Identifier(MOD_ID, "dismantle");
	public static final Identifier LAUNCH_PACKET_ID = new Identifier(MOD_ID, "launch");
	public static final Identifier TARGET_PACKET_ID = new Identifier(MOD_ID, "target");
	public static final Identifier TRANSMIT_PACKET_ID = new Identifier(MOD_ID, "transmit");
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		Registry.register(Registry.ITEM, new Identifier(ProjectCataclysmMod.MOD_ID, "silver_scythe"), ModItems.SILVER_SCYTHE);
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(SILVER_SCYTHE_INVENTORY));
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_ESSENCE, RenderLayer.getCutout());
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
				new Identifier(MOD_ID, "end_silver_ore"), END_SILVER_ORE_CONFIGURED_FEATURE);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MOD_ID, "end_silver_ore"),
				END_SILVER_ORE_PLACED_FEATURE);
		BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES,
				RegistryKey.of(Registry.PLACED_FEATURE_KEY,
						new Identifier(MOD_ID, "end_silver_ore")));
		ServerPlayNetworking.registerGlobalReceiver(DISMANTLE_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
			BlockPos pos =  buf.readBlockPos();
			World world = player.getWorld();
			BlockState state = world.getBlockState(pos);
			LaunchPlatformBlock.breakAndDrop(state, world, pos.offset(state.get(LaunchPlatformBlock.FACING).getOpposite(), 3));
		}));
		ServerPlayNetworking.registerGlobalReceiver(LAUNCH_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
			BlockPos padPos = buf.readBlockPos();
			if (player.world.getBlockState(padPos).isOf(ModBlocks.LAUNCH_PLATFORM)) {
				BlockPos sourcePos = padPos.offset(player.world.getBlockState(padPos).get(LaunchPlatformBlock.FACING).getOpposite(), 3);
				server.execute(() -> {
					if (player.world.getBlockEntity(sourcePos) != null) {
						((LaunchPlatformBlockEntity) Objects.requireNonNull(player.world.getBlockEntity(sourcePos))).launch();
					}
				});
			}
		}));
		ServerPlayNetworking.registerGlobalReceiver(TARGET_PACKET_ID, (((server, player, handler, buf, responseSender) -> {
			if (player.currentScreenHandler instanceof ControlPanelScreenHandler){
				((ControlPanelScreenHandler) player.currentScreenHandler).propertyDelegate.set(0, buf.readInt());
				((ControlPanelScreenHandler) player.currentScreenHandler).propertyDelegate.set(1, buf.readInt());
			}
		})));
		ServerPlayNetworking.registerGlobalReceiver(TRANSMIT_PACKET_ID, (((server, player, handler, buf, responseSender) -> {
			BlockPos source = buf.readBlockPos();
			int targetX = buf.readInt() + 107;
			int targetZ = buf.readInt() + 9;
			server.execute(() -> {
				LaunchPlatformBlockEntity launchPlatformBlockEntity = (LaunchPlatformBlockEntity) player.world.getBlockEntity(source);
				assert launchPlatformBlockEntity != null;
				launchPlatformBlockEntity.propertyDelegate.set(0, targetX);
				launchPlatformBlockEntity.propertyDelegate.set(1, targetZ);
			});
		})));
	}
}
