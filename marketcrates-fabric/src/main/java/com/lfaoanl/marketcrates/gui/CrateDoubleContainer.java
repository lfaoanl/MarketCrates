package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerType;

public class CrateDoubleContainer extends BaseCrateContainer {

    private static final ScreenHandlerType<CrateDoubleContainer> CONTAINER_REGISTRY = CrateRegistry.CRATE_DOUBLE_SCREEN;

    public CrateDoubleContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new SimpleInventory(12));

    }

    public CrateDoubleContainer(int id, PlayerInventory playerInventory, Inventory inventory) {
        super(id, playerInventory, inventory, 12, CONTAINER_REGISTRY, true);
    }
}