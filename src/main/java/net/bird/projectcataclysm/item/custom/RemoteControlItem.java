package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.block.custom.LaunchPlatformBlock;
import net.bird.projectcataclysm.block.custom.LaunchPlatformType;
import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.screen.ControlPanelScreenHandler;
import net.bird.projectcataclysm.screen.FabricatingScreenHandler;
import net.bird.projectcataclysm.screen.RemoteControlScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RemoteControlItem extends Item implements ExtendedScreenHandlerFactory {
    public RemoteControlItem(Settings settings) {
        super(settings);
    }

    private static final Text TITLE = Text.translatable("");

    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        stack.getOrCreateNbt();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.getOrCreateNbt().contains("SyncedX")) {
            BlockPos blockPos = new BlockPos(stack.getNbt().getInt("SyncedX"), stack.getNbt().getInt("SyncedY"), stack.getNbt().getInt("SyncedZ"));
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block instanceof LaunchPlatformBlock && blockState.get(LaunchPlatformBlock.TYPE) == LaunchPlatformType.CONTROL_PANEL) {
                user.sendMessage(Text.translatable("text"), true);
                user.openHandledScreen(this);
                return TypedActionResult.success(stack, world.isClient());
            }
            else {
                user.sendMessage(Text.translatable("block.projectcataclysm.remote_control.open_fail"), true);
                return TypedActionResult.fail(stack);
            }
        }
        else {
            user.sendMessage(Text.translatable("block.projectcataclysm.remote_control.open_unsynced"), true);
            return TypedActionResult.pass(stack);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        Block block = blockState.getBlock();
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        if (block instanceof LaunchPlatformBlock && blockState.get(LaunchPlatformBlock.TYPE) == LaunchPlatformType.CONTROL_PANEL) {
            if (stack.getOrCreateNbt().contains("SyncedX")) {
                if (stack.getNbt().getInt("SyncedX") == blockPos.getX() && stack.getNbt().getInt("SyncedY") == blockPos.getY() && stack.getNbt().getInt("SyncedZ") == blockPos.getZ()) {
                    stack.removeSubNbt("SyncedX");
                    stack.removeSubNbt("SyncedY");
                    stack.removeSubNbt("SyncedZ");
                    player.sendMessage(Text.translatable("block.projectcataclysm.remote_control.unsync"), true);
                    return ActionResult.success(world.isClient);
                }
            }
            stack.getNbt().putInt("SyncedX", blockPos.getX());
            stack.getNbt().putInt("SyncedY", blockPos.getY());
            stack.getNbt().putInt("SyncedZ", blockPos.getZ());
            player.sendMessage(Text.translatable("block.projectcataclysm.remote_control.sync"), true);
            return ActionResult.success(world.isClient);
        }
        return super.useOnBlock(context);
    }

    public boolean hasGlint(ItemStack stack) {
        return stack.getOrCreateNbt().contains("SyncedX");
    }

    /*public NamedScreenHandlerFactory createScreenHandlerFactory(World world, BlockPos pos) {
        return new ExtendedScreenHandlerFactory((syncId, inventory, player) -> new RemoteControlScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }*/

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        Hand hand;
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.REMOTE_CONTROL)) {
            hand = Hand.MAIN_HAND;
        }
        else {
            hand = Hand.OFF_HAND;
        }
        buf.writeBlockPos(new BlockPos(player.getStackInHand(hand).getNbt().getInt("SyncedX"), player.getStackInHand(hand).getNbt().getInt("SyncedY"), player.getStackInHand(hand).getNbt().getInt("SyncedZ")));
    }

    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RemoteControlScreenHandler(syncId, inv);
    }
}
