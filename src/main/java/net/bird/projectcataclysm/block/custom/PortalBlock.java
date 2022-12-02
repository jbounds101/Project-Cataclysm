package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.item.custom.PortalGunItem;
import net.bird.projectcataclysm.item.custom.PropelItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PortalBlock extends Block {

    static int wait = 0;

    public PortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        if (!PortalGunItem.isOrangePortalExists() || !PortalGunItem.isBluePortalExists()) {
            return;
        }
        if (!world.isClient()) {
            if (wait == 0) {
                if (state.getBlock().equals(ModBlocks.BLUE_PORTAL_BLOCK)) {

                    BlockPos orange = PortalGunItem.getOrangePortalPos();
                    entity.requestTeleport(orange.getX(), (orange.getY() + 1), orange.getZ());
                    wait = 1;
                } else if (state.getBlock().equals(ModBlocks.ORANGE_PORTAL_BLOCK)) {
                    BlockPos blue = PortalGunItem.getBluePortalPos();
                    entity.requestTeleport(blue.getX(), (blue.getY() + 1), blue.getZ());
                    wait = 1;
                }
                executorService.schedule(PortalBlock::cooldown, 3, TimeUnit.SECONDS);
            }
        }

        super.onSteppedOn(world, pos, state, entity);
    }




    public static void cooldown() {
        wait = 0;
    }
}
