package com.lfaoanl.marketcrates.blocks;

import com.lfaoanl.marketcrates.common.ItemOrientation;
import com.lfaoanl.marketcrates.core.CrateRegistry;
import com.lfaoanl.marketcrates.gui.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class CrateBlockEntity extends AbstractCrateBlockEntity {

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(CrateRegistry.CRATE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void sendContents() {
        if (!world.isClient) {
            // FORGE send packet to client
//            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) this.getLevel().getChunk(this.getBlockPos()));
//            CratesPacketHandler.INSTANCE.send(target, new CrateItemsPacket(this.getBlockPos(), ItemOrientation.toItemStack(stacks)));

            PacketByteBuf data = PacketByteBufs.create();
            BlockPos blockPos = getPos();
            data.writeBlockPos(blockPos);
            data.writeInt(stacks.size());

            for (ItemStack item : ItemOrientation.toItemStack(stacks)) {
                data.writeItemStack(item);
            }

            // Iterate over all players tracking a position in the world and send the packet to each player
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
                ServerPlayNetworking.send(player, CrateRegistry.CRATE_CHANNEL, data);

            }
        }
    }

    @Override
    protected ScreenHandler createScreenHandler(int id, PlayerInventory player) {
        if (isDoubleCrate()) {
            return new CrateDoubleContainer(id, player, this);
        }
        return new CrateContainer(id, player, this);
    }
}
