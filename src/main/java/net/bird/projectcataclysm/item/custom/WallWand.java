package net.bird.projectcataclysm.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WallWand extends Item {
    public WallWand(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();
        user.sendMessage(Text.literal("I AM KAKASHI THE COPY NINJA I KNOW A THOUSAND JUTSU"));
        BlockPos positionClicked = context.getBlockPos();
        int x = positionClicked.getX();

        int y = positionClicked.getY() + 1;
        int z = positionClicked.getZ();
        BlockPos positionClicked2 = new BlockPos(x, y, z);
        //positionClicked.add(100, 100, 100);
        float dir = context.getPlayerFacing().asRotation();

        //user.sendMessage(Text.literal(String.valueOf(dir)));
        if (dir == 0 || dir == 180) {
            for (int x2 = 0; x2 < 3; x2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x + x2, y + y2, z);
                    context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                }

            }
            for (int x2 = -3; x2 <= 0; x2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x + x2, y + y2, z);
                    context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                }
            }
        } else if (dir == 270 || dir == 90) {
            for (int z2 = 0; z2 < 3; z2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x, y + y2, z + z2);
                    context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                }

            }
            for (int z2 = -3; z2 <= 0; z2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x, y + y2, z + z2);
                    context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                }
            }

        }

        assert user != null;
        user.getItemCooldownManager().set(this, 20);
        return ActionResult.PASS;
    }






}
