package net.bird.projectcataclysm.screen;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.item.ModItems;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class ControlPanelScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Inventory head;
    private final Inventory tail;
    private final Inventory payload;
    private final HeadSlot headSlot;
    private final TailSlot tailSlot;
    private final PayloadSlot payloadSlot;
    private BlockPos pos;

    public ControlPanelScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
        this.pos = buf.readBlockPos();
    }
    public ControlPanelScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(ProjectCataclysmMod.CONTROL_PANEL_HANDLER, syncId);
        this.context = context;

        this.head = new SimpleInventory(1) {
            public boolean isValid(int slot, ItemStack stack) {
                return stack.isOf(ModItems.MISSILE_HEAD);
            }
            public int getMaxCountPerStack() {
                return 1;
            }
        };
        this.tail = new SimpleInventory(1) {
            public boolean isValid(int slot, ItemStack stack) {
                return stack.isOf(ModItems.MISSILE_TAIL);
            }
            public int getMaxCountPerStack() {
                return 1;
            }
        };
        this.payload = new SimpleInventory(1) {
            public boolean isValid(int slot, ItemStack stack) {
                return stack.isIn(ProjectCataclysmMod.MISSILE_PAYLOADS);
            }
            public int getMaxCountPerStack() {
                return 64;
            }
        };
        this.headSlot = new HeadSlot(this.head, 0, 47, 35);
        this.payloadSlot = new PayloadSlot(this.payload, 0, 47, 64);
        this.tailSlot = new TailSlot(this.tail, 0, 47, 93);
        this.addSlot(this.headSlot);
        this.addSlot(this.payloadSlot);
        this.addSlot(this.tailSlot);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 36 + j * 18, 139 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 36 + i * 18, 197));
        }
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0 || index == 1 || index == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            else if (!this.headSlot.hasStack() && this.headSlot.canInsert(itemStack2) && itemStack2.getCount() == 1) {
                if (!this.insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.payloadSlot.hasStack() && this.payloadSlot.canInsert(itemStack2)) {
                if (!this.insertItem(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.tailSlot.hasStack() && this.tailSlot.canInsert(itemStack2) && itemStack2.getCount() == 1) {
                if (!this.insertItem(itemStack2, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 3 && index < 30) {
                if (!this.insertItem(itemStack2, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 30 && index < 39) {
                if (!this.insertItem(itemStack2, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.insertItem(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.LAUNCH_PLATFORM);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.head);
            this.dropInventory(player, this.tail);
            this.dropInventory(player, this.payload);
        });
    }

    public void launch() {
        head.getStack(0).decrement(1);
        tail.getStack(0).decrement(1);
        payload.getStack(0).decrement(1);
    }

    public boolean canLaunch() {
        return hasHead() && hasTail() && hasPayload();
    }

    public boolean hasHead() {
        return !this.head.getStack(0).isEmpty();
    }

    public boolean hasTail() {
        return !this.tail.getStack(0).isEmpty();
    }

    public boolean hasPayload() {
        return !this.payload.getStack(0).isEmpty();
    }

    public ItemStack getPayload() {
        return this.payload.getStack(0);
    }

    private static class HeadSlot extends Slot {
        public HeadSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isOf(ModItems.MISSILE_HEAD);
        }

        public int getMaxItemCount() {
            return 1;
        }
    }

    private static class TailSlot extends Slot {
        public TailSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isOf(ModItems.MISSILE_TAIL);
        }

        public int getMaxItemCount() {
            return 1;
        }
    }

    private static class PayloadSlot extends Slot {
        public PayloadSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isIn(ProjectCataclysmMod.MISSILE_PAYLOADS);
        }

    }

    public BlockPos getPos() {
        return pos;
    }
}
