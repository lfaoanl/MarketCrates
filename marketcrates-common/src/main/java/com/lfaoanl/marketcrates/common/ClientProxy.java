package com.lfaoanl.marketcrates.common;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

public class ClientProxy implements IMarketCratesProxy {

    @Override
    public World getWorld() {
        return MinecraftClient.getInstance().world;
    }
}
