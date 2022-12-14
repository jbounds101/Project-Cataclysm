package net.bird.projectcataclysm.block.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.ModBlocks;
import net.bird.projectcataclysm.screen.ControlPanelScreen;
import net.bird.projectcataclysm.screen.ControlPanelScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LaunchPlatformBlock extends BlockWithEntity {
    public static final EnumProperty<LaunchPlatformType> TYPE;
    public static final DirectionProperty FACING;
    public static final BooleanProperty TRIGGERED;
    protected static final VoxelShape BOTTOM_SHAPE;
    protected static final VoxelShape NORTH_WEST_CORNER_SHAPE;
    protected static final VoxelShape SOUTH_WEST_CORNER_SHAPE;
    protected static final VoxelShape NORTH_EAST_CORNER_SHAPE;
    protected static final VoxelShape SOUTH_EAST_CORNER_SHAPE;

    protected static final VoxelShape NORTH_STAIR_SHAPE;
    protected static final VoxelShape SOUTH_STAIR_SHAPE;
    protected static final VoxelShape WEST_STAIR_SHAPE;
    protected static final VoxelShape EAST_STAIR_SHAPE;
    protected static final VoxelShape NORTH_SHAPE;
    protected static final VoxelShape SOUTH_SHAPE;
    protected static final VoxelShape WEST_SHAPE;
    protected static final VoxelShape EAST_SHAPE;
    protected static final VoxelShape BASE_SHAPE;
    public LaunchPlatformBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(TRIGGERED, false));
    }

    public static void breakAndDrop(BlockState state, World world, BlockPos blockPos) {
        Direction forwards = state.get(FACING);
        Direction left = forwards.rotateYCounterclockwise();
        Direction right = forwards.rotateYClockwise();
        Direction backwards = forwards.getOpposite();
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
        dropStack(world, blockPos, new ItemStack(ModBlocks.LAUNCH_PLATFORM));
        blockPos = blockPos.offset(left, 2).offset(forwards);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                blockPos = blockPos.offset(forwards);
            }
            blockPos = blockPos.offset(backwards, 5).offset(right);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(TYPE) == LaunchPlatformType.CONTROL_PANEL) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            }
            else {
                if (world.getBlockEntity(pos) instanceof LaunchPlatformBlockEntity) {
                    player.openHandledScreen((NamedScreenHandlerFactory) world.getBlockEntity(pos));
                }
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (state.get(TYPE) == LaunchPlatformType.CONTROL_PANEL) {
            if (world.isReceivingRedstonePower(pos) && !state.get(TRIGGERED)) {
                world.createAndScheduleBlockTick(pos, this, 4);
                world.setBlockState(pos, state.with(TRIGGERED, true), Block.NO_REDRAW);
            } else if (!world.isReceivingRedstonePower(pos) && state.get(TRIGGERED)) {
                world.setBlockState(pos, state.with(TRIGGERED, false), Block.NO_REDRAW);
            }
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        LaunchPlatformBlockEntity launchPlatformBlockEntity = (LaunchPlatformBlockEntity) world.getBlockEntity(pos);
        assert launchPlatformBlockEntity != null;
        launchPlatformBlockEntity.launch();
    }

    public boolean hasSidedTransparency(BlockState state) {
        return state.get(TYPE) != LaunchPlatformType.BASE;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(TYPE)) {
            case STRAIGHT_STAIRS: {
                return switch (state.get(FACING)) {
                    case NORTH -> NORTH_STAIR_SHAPE;
                    case SOUTH -> SOUTH_STAIR_SHAPE;
                    case WEST -> WEST_STAIR_SHAPE;
                    case EAST -> EAST_STAIR_SHAPE;
                    default -> VoxelShapes.fullCube();
                };
            }
            case OUTER_STAIRS: {
                return switch (state.get(FACING)) {
                    case NORTH -> NORTH_EAST_CORNER_SHAPE;
                    case SOUTH -> SOUTH_WEST_CORNER_SHAPE;
                    case WEST -> NORTH_WEST_CORNER_SHAPE;
                    case EAST -> SOUTH_EAST_CORNER_SHAPE;
                    default -> VoxelShapes.fullCube();
                };
            }
            case CONTROL_PANEL: {
                return switch (state.get(FACING)) {
                    case NORTH -> NORTH_SHAPE;
                    case SOUTH -> SOUTH_SHAPE;
                    case WEST -> WEST_SHAPE;
                    case EAST -> EAST_SHAPE;
                    default -> BASE_SHAPE;
                };
            }
            default: return VoxelShapes.fullCube();
        }
    }
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction forwards = ctx.getPlayerFacing();
        Direction left = forwards.rotateYCounterclockwise();
        Direction right = forwards.rotateYClockwise();
        Direction backwards = forwards.getOpposite();
        boolean canPlace = blockPos.getY() < world.getTopY() - 4;
        blockPos = blockPos.offset(left, 2).offset(forwards);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    canPlace = canPlace && world.getBlockState(blockPos).canReplace(ctx);
                    if (!canPlace) {
                        Objects.requireNonNull(ctx.getPlayer()).sendMessage(Text.translatable("block.projectcataclysm.launch_platform.failed_placement"), true);
                        return null;
                    }
                    blockPos = blockPos.offset(forwards);
                }
                blockPos = blockPos.offset(backwards, 5).offset(right);
            }
            blockPos = blockPos.offset(left, 5).up();
        }
        return this.getDefaultState().with(FACING, forwards).with(TYPE, LaunchPlatformType.CONTROL_PANEL);
    }
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos blockPos = pos;
        Direction forwards = state.get(FACING);
        Direction left = forwards.rotateYCounterclockwise();
        Direction right = forwards.rotateYClockwise();
        Direction backwards = forwards.getOpposite();
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(left);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(left);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.OUTER_STAIRS), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, right).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, right).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, right).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, right).with(TYPE, LaunchPlatformType.OUTER_STAIRS), 3);
        blockPos = blockPos.offset(right);
        world.setBlockState(blockPos, state.with(FACING, backwards).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(right);
        world.setBlockState(blockPos, state.with(FACING, backwards).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(right);
        world.setBlockState(blockPos, state.with(FACING, backwards).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(right);
        world.setBlockState(blockPos, state.with(FACING, backwards).with(TYPE, LaunchPlatformType.OUTER_STAIRS), 3);
        blockPos = blockPos.offset(backwards);
        world.setBlockState(blockPos, state.with(FACING, left).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(backwards);
        world.setBlockState(blockPos, state.with(FACING, left).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(backwards);
        world.setBlockState(blockPos, state.with(FACING, left).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(backwards);
        world.setBlockState(blockPos, state.with(FACING, left).with(TYPE, LaunchPlatformType.OUTER_STAIRS), 3);
        blockPos = blockPos.offset(left);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.STRAIGHT_STAIRS), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(left);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(backwards); // center
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(backwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(left);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
        blockPos = blockPos.offset(forwards);
        world.setBlockState(blockPos, state.with(FACING, forwards).with(TYPE, LaunchPlatformType.BASE), 3);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, TRIGGERED);
    }

    static {
        TYPE = EnumProperty.of("type", LaunchPlatformType.class);
        FACING = HorizontalFacingBlock.FACING;
        TRIGGERED = Properties.TRIGGERED;
        BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        NORTH_WEST_CORNER_SHAPE = VoxelShapes.union(BOTTOM_SHAPE, Block.createCuboidShape(0.0, 8.0, 0.0, 8.0, 16.0, 8.0));
        SOUTH_WEST_CORNER_SHAPE = VoxelShapes.union(BOTTOM_SHAPE, Block.createCuboidShape(0.0, 8.0, 8.0, 8.0, 16.0, 16.0));
        NORTH_EAST_CORNER_SHAPE = VoxelShapes.union(BOTTOM_SHAPE, Block.createCuboidShape(8.0, 8.0, 0.0, 16.0, 16.0, 8.0));
        SOUTH_EAST_CORNER_SHAPE = VoxelShapes.union(BOTTOM_SHAPE, Block.createCuboidShape(8.0, 8.0, 8.0, 16.0, 16.0, 16.0));
        NORTH_STAIR_SHAPE = VoxelShapes.union(NORTH_WEST_CORNER_SHAPE, NORTH_EAST_CORNER_SHAPE);
        SOUTH_STAIR_SHAPE = VoxelShapes.union(SOUTH_WEST_CORNER_SHAPE, SOUTH_EAST_CORNER_SHAPE);
        WEST_STAIR_SHAPE = VoxelShapes.union(NORTH_WEST_CORNER_SHAPE, SOUTH_WEST_CORNER_SHAPE);
        EAST_STAIR_SHAPE = VoxelShapes.union(NORTH_EAST_CORNER_SHAPE, SOUTH_EAST_CORNER_SHAPE);
        BASE_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 14.0, 12.0));
        EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(1.0, 10.0, 0.0, 5.333333, 14.0, 16.0), Block.createCuboidShape(5.333333, 12.0, 0.0, 9.666667, 16.0, 16.0), Block.createCuboidShape(9.666667, 14.0, 0.0, 14.0, 18.0, 16.0), BASE_SHAPE);
        SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 10.0, 1.0, 16.0, 14.0, 5.333333), Block.createCuboidShape(0.0, 12.0, 5.333333, 16.0, 16.0, 9.666667), Block.createCuboidShape(0.0, 14.0, 9.666667, 16.0, 18.0, 14.0), BASE_SHAPE);
        WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(10.666667, 10.0, 0.0, 15.0, 14.0, 16.0), Block.createCuboidShape(6.333333, 12.0, 0.0, 10.666667, 16.0, 16.0), Block.createCuboidShape(2.0, 14.0, 0.0, 6.333333, 18.0, 16.0), BASE_SHAPE);
        NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 10.0, 10.666667, 16.0, 14.0, 15.0), Block.createCuboidShape(0.0, 12.0, 6.333333, 16.0, 16.0, 10.666667), Block.createCuboidShape(0.0, 14.0, 2.0, 16.0, 18.0, 6.333333), BASE_SHAPE);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaunchPlatformBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
