package com.lfaoanl.marketcrates.gui;

import com.lfaoanl.marketcrates.core.CrateRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerType;


public class CrateContainer extends BaseCrateContainer {


    private static final ScreenHandlerType<CrateContainer> CONTAINER_REGISTRY = CrateRegistry.CRATE_SCREEN;

    public CrateContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new SimpleInventory(6));

    }

    public CrateContainer(int id, PlayerInventory playerInventory, Inventory inventory) {
        super(id, playerInventory, inventory, 6, CONTAINER_REGISTRY, false);
    }

}
