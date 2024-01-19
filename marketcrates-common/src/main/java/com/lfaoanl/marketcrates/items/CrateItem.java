package com.lfaoanl.marketcrates.items;

import com.lfaoanl.marketcrates.blocks.AbstractCrateBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrateItem extends BlockItem {

    public CrateItem(Block blockIn, Settings builder) {
        super(blockIn, builder);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();

        // If item is same as BlockItem of targetted CrateBlock
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof AbstractCrateBlock && ((AbstractCrateBlock) block).getBlockItem().getItem().equals(this)) {

            // If the TileEntity is not already a double crate
//            BlockEntity tileEntity = world.getBlockEntity(pos);
//            if (tileEntity instanceof CrateTileEntity && !((CrateTileEntity) tileEntity).isDoubleCrate()) {
//                BlockState newState = tileEntity.getBlockState().setValue(CrateBlock.TYPE, CrateType.DOUBLE);
//                Player player = context.getPlayer();
//
//                world.setBlock(pos, newState, 2);
//                ((CrateBlock) tileEntity.getBlockState().getBlock()).playSound(world, pos, player, SoundEvents.WOOD_PLACE);
//
//                if (!player.isCreative()) {
//                    context.getItemInHand().setCount(context.getItemInHand().getCount() - 1);
//                }
//
//                // SUCCES when client && COSNUME when server
//                return InteractionResult.sidedSuccess(world.isClientSide());
//            }
        }

        return super.useOnBlock(context);
    }
}
