package com.lfaoanl.marketcrates.blocks;

import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.blocks.states.CrateTypeProperty;
import com.lfaoanl.marketcrates.common.ItemOrientation;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.tick.OrderedTick;

import java.util.List;

public abstract class AbstractCrateBlock extends BlockWithEntity implements Waterloggable {

    public static final Settings properties = Settings.create().strength(2.5F).sounds(BlockSoundGroup.WOOD);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final CrateTypeProperty TYPE = CrateTypeProperty.create("type");

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final Item inclinable = Items.STICK;

    private DefaultedList<ItemOrientation> oldItems;

    public AbstractCrateBlock() {
        super(properties);

        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(TYPE, CrateType.DEFAULT)
                .with(WATERLOGGED, false));


    }

    public AbstractCrateBlock(Settings settings) {
        super(settings);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(TYPE);
        builder.add(WATERLOGGED);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getFluidTickScheduler().scheduleTick(OrderedTick.create(Fluids.WATER, currentPos));
        }

        return super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView worldIn, BlockPos pos, NavigationType type) {
        return false;
    }


    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    /**
     * Opacity 1 to fix the lighting on the block below
     *
     * @param state
     * @param worldIn
     * @param pos
     * @return
     */
    @Override
    public int getOpacity(BlockState state, BlockView worldIn, BlockPos pos) {
        return 1;
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = context.getWorld().getBlockState(blockPos);
        FluidState fluidstate = context.getWorld().getFluidState(context.getBlockPos());
        boolean isWaterLogged = fluidstate.getFluid() == Fluids.WATER;

        if (blockState.isOf(this)) {
            return (BlockState)((BlockState)blockState.with(TYPE, CrateType.DOUBLE)).with(WATERLOGGED, isWaterLogged);
        }
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite()).with(WATERLOGGED, isWaterLogged);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        boolean horizontal = false;

        if (state.get(FACING).getAxis() == Direction.Axis.X) {
            horizontal = true;
        }

        return state.get(TYPE).getShape(horizontal);
    }

    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.get(FACING)));
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack itemStack = context.getStack();
        CrateType crateType = state.get(TYPE);
        if (crateType == CrateType.DOUBLE || !itemStack.isOf(this.asItem())) {
            return false;
        }
        if (context.canReplaceExisting()) {
            boolean bl = context.getHitPos().y - (double)context.getBlockPos().getY() > 0.5;
            Direction direction = context.getSide();
            if (crateType == CrateType.DEFAULT) {
                return direction == Direction.UP || bl && direction.getAxis().isHorizontal();
            }
            return direction == Direction.DOWN || !bl && direction.getAxis().isHorizontal();
        }
        return true;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            // System.out.println("onReplaced");
            BlockEntity tileentity = world.getBlockEntity(pos);

            if (tileentity instanceof AbstractCrateBlockEntity) {
                if (state.get(TYPE).isDouble()) {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), getBlockItem());
                }
                ItemScatterer.spawn(world, pos, (AbstractCrateBlockEntity) tileentity);
//                worldIn.updateComparatorOutputLevel(pos, this); TODO

                this.oldItems = ((AbstractCrateBlockEntity) world.getBlockEntity(pos)).getItems();
            }
        }
        super.onStateReplaced(state, world, pos, newState, isMoving);
    }

    public ItemStack getBlockItem() {
        return new ItemStack(Item.fromBlock(this));
    }

    //@Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if (state.get(TYPE).isDouble()) {
            for (int i = 0; i < 12; i++) {
                ItemScatterer.spawn((World) world, pos.getX(), pos.getY(), pos.getZ(), oldItems.get(i).getItemStack());
            }
            oldItems = null;
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit) {

        CrateType crateType = state.get(TYPE);
        ItemStack heldItem = player.getMainHandStack();
        if (!crateType.isDouble() && player.isCreative() && heldItem.getItem() == inclinable) {
            world.setBlockState(pos, state.with(TYPE, crateType.opposite()));
            return ActionResult.CONSUME;
        }

        if (!world.isClient) {

            openGui(state, world, player, pos);
            return ActionResult.CONSUME;
        }

        return ActionResult.SUCCESS;
    }

    protected abstract void openGui(BlockState state, World world, PlayerEntity player, BlockPos pos);

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        CrateType crateType = state.get(TYPE);
        ItemStack heldItem = player.getMainHandStack();

        if (!player.isCreative() && !crateType.isDouble() && hasProperItem(crateType, heldItem)) {

            SoundEvent blockSound = SoundEvents.BLOCK_WOOD_BREAK;

            // Checks to see the current state and switches it
            if (crateType == CrateType.DEFAULT) {
                // Crate becomes inclined
                heldItem.setCount(heldItem.getCount() - 1);
                blockSound = SoundEvents.BLOCK_WOOD_PLACE;
            } else {
                // Crate becomes default
                player.dropItem(new ItemStack(inclinable), false);
            }

            playSound(world, pos, player, blockSound);
            world.setBlockState(pos, state.with(TYPE, crateType.opposite()));
        }
    }

    public void playSound(World world, BlockPos pos, PlayerEntity player, SoundEvent blockSound) {
        world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), blockSound, SoundCategory.BLOCKS, 0.8f, 1.5f);
    }

    /**
     * Checks to see if the user has te proper item to turn an inclined crate back to a normal crate
     *
     * @param heldItem
     * @return
     */
    private boolean hasProperTool(ItemStack heldItem) {
        return heldItem.getItem() instanceof AxeItem;
    }

    /**
     * Checks to see if the user is holding the inclinable item (stick)
     * Or when the crate is already inclined if it is holding the hasProperTool
     *
     * @param type
     * @param heldItem
     * @return
     */
    private boolean hasProperItem(CrateType type, ItemStack heldItem) {
        if (type == CrateType.INCLINED) {
            return hasProperTool(heldItem);
        }
        return heldItem.getItem() == inclinable && heldItem.getCount() >= 1;
    }

    /**
     * Override de BaseBlockEntity's method which renders the model INVISIBLE
     *
     * @param blockState
     * @return
     */
    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }


}