package com.lfaoanl.marketcrates.client;

import com.lfaoanl.marketcrates.blocks.AbstractCrateBlockEntity;
import com.lfaoanl.marketcrates.common.ClientProxy;
import com.lfaoanl.marketcrates.common.MarketCrates;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.gui.CrateDoubleScreen;
import com.lfaoanl.marketcrates.gui.CrateScreen;
import com.lfaoanl.marketcrates.render.CrateBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class MarketCratesFabricClient extends ClientProxy implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MarketCrates.proxy = this;

        ScreenRegistry.register(CrateRegistry.CRATE_SCREEN, CrateScreen::new);
        ScreenRegistry.register(CrateRegistry.CRATE_DOUBLE_SCREEN, CrateDoubleScreen::new);

        BlockEntityRendererRegistry.INSTANCE.register(CrateRegistry.CRATE_BLOCK_ENTITY, CrateBlockEntityRenderer::new);

        registerClientPacketReceiver();
    }

    private void registerClientPacketReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(CrateRegistry.CRATE_CHANNEL, (client, handler, buf, responseSender) -> {
            MarketCrates.LOGGER.info("Client receiver initialised");

            // Read packet data on the event loop
            BlockPos target = buf.readBlockPos();
            int itemsSize = buf.readInt();
            DefaultedList<ItemStack> items = DefaultedList.of();
            for (int i = 0; i < itemsSize; i++) {
                items.add(i, buf.readItemStack());
            }

            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                World world = MarketCrates.proxy.getWorld();

                if (world != null) {
                    BlockEntity tile = world.getBlockEntity(target);

                    if (tile instanceof AbstractCrateBlockEntity) {

                        ((AbstractCrateBlockEntity) tile).receiveContents(items);
                    }
                }
            });
        });
    }

}