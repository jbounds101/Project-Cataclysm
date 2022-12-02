package net.bird.projectcataclysm.screen;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.custom.LaunchPlatformBlock;
import net.bird.projectcataclysm.block.custom.LaunchPlatformBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RemoteControlScreenHandler extends ScreenHandler {
    private BlockPos pos;

    private BlockPos source;

    private PlayerEntity player;

    public RemoteControlScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory);
        this.source = buf.readBlockPos();
        this.player = playerInventory.player;
        this.pos = source.offset(player.world.getBlockState(source).get(LaunchPlatformBlock.FACING), 3);
    }

    public RemoteControlScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ProjectCataclysmMod.REMOTE_CONTROL_HANDLER, syncId);
    }

    public LaunchPlatformBlockEntity getControlPanel() {
        return (LaunchPlatformBlockEntity) player.world.getBlockEntity(source);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockPos getSource() {
        return source;
    }
}
