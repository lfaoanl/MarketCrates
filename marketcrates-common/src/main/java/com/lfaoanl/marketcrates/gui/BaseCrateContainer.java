package com.lfaoanl.marketcrates.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class BaseCrateContainer extends ScreenHandler {


    protected final Inventory inventory;
    private final int size;

    public BaseCrateContainer(int id, PlayerInventory playerInventory, Inventory inventory, int size, ScreenHandlerType<?extends BaseCrateContainer> containerType, boolean isDouble) {
        super(containerType, id);

        if (isDouble) {
            // Left
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    this.addSlot(new Slot(inventory, i + j * 3, 35 + j * 18, 17 + i * 18));
                }
            }

            // Right
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    this.addSlot(new Slot(inventory, 6 + i + j * 3, 107 + j * 18, 17 + i * 18));
                }
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    this.addSlot(new Slot(inventory, i + j * 3, 71 + j * 18, 17 + i * 18));
                }
            }
        }

        this.size = size;

        checkSize(inventory, size);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        drawUserInventory(playerInventory);

    }

    private void drawContainerSlots() {
        //TODO dynamically calculate x values with an offset
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 2; ++j) {
//                this.addSlot(new Slot(inventory, i + j * 3, 71 + j * 18, 17 + i * 18));
//            }
//        }
    }

    private void drawUserInventory(PlayerInventory playerInventory) {
        // Player inventory
        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        // Hotbar
        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canUse(PlayerEntity playerIn) {
        return this.inventory.canPlayerUse(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMove(PlayerEntity playerIn, int slotIndex) {

        // Init empty itemstack
        ItemStack copyStack = ItemStack.EMPTY;

        // Get significant slot
        Slot slot = this.slots.get(slotIndex);

        // If slot has stuff
        if (slot != null && slot.hasStack()) {

            // Get stuff from slot
            ItemStack stackFromSlot = slot.getStack();
            copyStack = stackFromSlot.copy();


            int playerInventorySize = this.slots.size() - size;
            if (slotIndex >= playerInventorySize) {

                // If slot is changed
                if (!this.insertItem(stackFromSlot, 0, playerInventorySize, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stackFromSlot, playerInventorySize, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackFromSlot.isEmpty()) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (stackFromSlot.getCount() == copyStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(playerIn, stackFromSlot);
        }

        return copyStack;
    }

    /**
     * Called when the container is closed.
     */
    public void onClosed(PlayerEntity playerIn) {
        super.onClosed(playerIn);
        this.inventory.onClose(playerIn);
    }

}
