package net.bird.projectcataclysm.screen;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.recipe.FabricatingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class FabricatingScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
    private final CraftingInventory input;
    private final CraftingResultInventory result;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public FabricatingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public FabricatingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ProjectCataclysmMod.FABRICATING_HANDLER, syncId);
        this.input = new CraftingInventory(this, 5, 5);
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, 0, 160, 53));

        int i;
        int j;
        for(i = 0; i < 5; ++i) {
            for(j = 0; j < 5; ++j) {
                this.addSlot(new Slot(this.input, j + i * 5, 30 + j * 18, 17 + i * 18));
            }
        }

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 14 + j * 18, 120 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 14 + i * 18, 178));
        }

    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<FabricatingRecipe> optional = Objects.requireNonNull(world.getServer()).getRecipeManager().getFirstMatch(ProjectCataclysmMod.FABRICATING, craftingInventory, world);
            if (optional.isPresent()) {
                FabricatingRecipe fabricatingRecipe = optional.get();
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, fabricatingRecipe)) {
                    itemStack = fabricatingRecipe.craft(craftingInventory);
                }
            }

            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> updateResult(this, world, this.player, this.input, this.result));
    }

    public void populateRecipeFinder(RecipeMatcher finder) {
        this.input.provideRecipeInputs(finder);
    }

    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        return recipe.matches(this.input, this.player.world);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.FABRICATOR);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                this.context.run((world, pos) -> itemStack2.getItem().onCraft(itemStack2, world, player));
                if (!this.insertItem(itemStack2, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index >= 26 && index < 62) {
                if (!this.insertItem(itemStack2, 1, 26, false)) {
                    if (index < 53) {
                        if (!this.insertItem(itemStack2, 53, 62, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 26, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 26, 62, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
            if (index == 0) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    public int getCraftingResultSlotIndex() {
        return 0;
    }

    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    public int getCraftingSlotCount() {
        return 26;
    }

    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }

    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }
}