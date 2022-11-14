package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.screen.ControlPanelScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class LaunchPlatformBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, Inventory {
    private final BlockState state;
    private static final Text TITLE = Text.translatable("container.control_panel");
    protected DefaultedList<ItemStack> inventory;
    protected int targetX;
    protected int targetZ;
    protected final PropertyDelegate propertyDelegate = new PropertyDelegate(){
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> LaunchPlatformBlockEntity.this.targetX;
                case 1 -> LaunchPlatformBlockEntity.this.targetZ;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> LaunchPlatformBlockEntity.this.targetX = value;
                case 1 -> LaunchPlatformBlockEntity.this.targetZ = value;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public LaunchPlatformBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.LAUNCH_PLATFORM_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.state = state;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos.offset(state.get(LaunchPlatformBlock.FACING), 3));
    }

    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ControlPanelScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.targetX = nbt.getInt("TargetX");
        this.targetZ = nbt.getInt("TargetZ");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("TargetX", this.targetX);
        nbt.putInt("TargetZ", this.targetZ);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        assert this.world != null;
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 0) {
            return stack.isOf(ModItems.MISSILE_HEAD);
        } else if (slot == 1) {
            return stack.isIn(ProjectCataclysmMod.MISSILE_PAYLOADS);
        } else {
            return stack.isOf(ModItems.MISSILE_TAIL);
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

}
