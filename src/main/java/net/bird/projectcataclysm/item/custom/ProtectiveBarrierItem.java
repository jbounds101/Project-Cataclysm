package net.bird.projectcataclysm.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProtectiveBarrierItem extends Item {
    public ProtectiveBarrierItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        stack.getOrCreateNbt();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        int x, y, z;
        BlockState placedBlock;
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient()) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 1, false, false, true));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 0, false, false, true));
            if (!stack.getOrCreateNbt().contains("DeployedX")) {
                BlockPos usedPos = user.getBlockPos();
                x = usedPos.getX();
                stack.getOrCreateNbt().putInt("DeployedX", x);
                y = usedPos.getY() + 1;
                stack.getOrCreateNbt().putInt("DeployedY", y);
                z = usedPos.getZ();
                stack.getOrCreateNbt().putInt("DeployedZ", z);
                placedBlock = Blocks.OBSIDIAN.getDefaultState();
            } else {
                x = stack.getOrCreateNbt().getInt("DeployedX");
                y = stack.getOrCreateNbt().getInt("DeployedY");
                z = stack.getOrCreateNbt().getInt("DeployedZ");
                stack.removeSubNbt("DeployedX");
                stack.removeSubNbt("DeployedY");
                stack.removeSubNbt("DeployedZ");
                placedBlock = Blocks.AIR.getDefaultState();
            }
            BlockPos placePos;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    placePos = new BlockPos(x + i, y + j, z + 2);
                    if (!world.getBlockState(placePos).isIn(BlockTags.WITHER_IMMUNE)) {
                        world.setBlockState(placePos, placedBlock, 2);
                    }
                }
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    placePos = new BlockPos(x + i, y + j, z - 2);
                    if (!world.getBlockState(placePos).isIn(BlockTags.WITHER_IMMUNE)) {
                        world.setBlockState(placePos, placedBlock, 2);
                    }
                }
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    placePos = new BlockPos(x + i, y + 2, z + j);
                    if (!world.getBlockState(placePos).isIn(BlockTags.WITHER_IMMUNE)) {
                        world.setBlockState(placePos, placedBlock, 2);
                    }
                }
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    placePos = new BlockPos(x + i, y - 2, z + j);
                    if (!world.getBlockState(placePos).isIn(BlockTags.WITHER_IMMUNE)) {
                        world.setBlockState(placePos, placedBlock, 2);
                    }
                }
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    placePos = new BlockPos(x + 2, y + j, z + i);
                    if (!world.getBlockState(placePos).isIn(BlockTags.WITHER_IMMUNE)) {
                        world.setBlockState(placePos, placedBlock, 2);
                    }
                }
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    placePos = new BlockPos(x - 2, y + j, z + i);
                    if (!world.getBlockState(placePos).isIn(BlockTags.WITHER_IMMUNE)) {
                        world.setBlockState(placePos, placedBlock, 2);
                    }
                }
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}
