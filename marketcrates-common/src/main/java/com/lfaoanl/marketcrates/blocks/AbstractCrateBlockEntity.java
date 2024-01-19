package com.lfaoanl.marketcrates.blocks;

import com.lfaoanl.marketcrates.common.ItemOrientation;
import com.lfaoanl.marketcrates.common.MarketCrates;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractCrateBlockEntity extends LockableContainerBlockEntity {

    //    private NonNullList<ItemStack> stacks = NonNullList.withSize(6, ItemStack.EMPTY);
    protected DefaultedList<ItemOrientation> stacks = DefaultedList.ofSize(12, ItemOrientation.EMPTY);

    private boolean isDouble = false;

    protected AbstractCrateBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }


    public DefaultedList<ItemOrientation> getItems() {
        return stacks;
    }

    public boolean isDoubleCrate() {
        if (getCachedState().isAir()) {
            return this.isDouble;
        }
        // Store items to be able to retreive it after the block is broken
        this.isDouble = getCachedState().get(AbstractCrateBlock.TYPE).isDouble();

        return this.isDouble;
    }

    public void setStack(int index, ItemStack stack) {
        this.getItems().set(index, new ItemOrientation(stack));
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemOrientation::isEmpty);
    }

    public ItemStack getStack(int index) {
        // TODO increase size of `stacks` and check if crateIsDouble to allow the supplied index
        return this.getItems().get(index).getItemStack();
    }

    public ItemStack removeStack(int index, int count) {
        ItemStack itemstack = Inventories.splitStack(ItemOrientation.toItemStack(stacks), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        sendContents();
    }

    public ItemStack removeStack(int index) {
        ItemOrientation orientation = index >= 0 && index < stacks.size() ? stacks.set(index, ItemOrientation.EMPTY) : ItemOrientation.EMPTY;

        return orientation.getItemStack();
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.crate");
    }

    @Override
    public int size() {
//        if (isDoubleCrate()) {
        return 12;
//        }
//        return 6;
    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    public void clear() {
        this.getItems().clear();
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.stacks = loadFromNbt(nbt);
    }

    private DefaultedList<ItemOrientation> loadFromNbt(NbtCompound nbt) {
        DefaultedList<ItemStack> items = DefaultedList.ofSize(size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, items);
        return ItemOrientation.toItemOrientation(items);
    }

    public void writeNbt(NbtCompound compound) {
        super.writeNbt(compound);

        Inventories.writeNbt(compound, ItemOrientation.toItemStack(this.stacks));
    }

    public void receiveContents(DefaultedList<ItemStack> stacks) {
        MarketCrates.LOGGER.debug("Received packets");
        MarketCrates.LOGGER.debug(stacks.get(0));

        this.stacks = ItemOrientation.toItemOrientation(stacks);
    }

    public abstract void sendContents();

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound updateTag = super.toInitialChunkDataNbt();

        Inventories.writeNbt(updateTag, ItemOrientation.toItemStack(stacks));

        return updateTag;
    }

}
