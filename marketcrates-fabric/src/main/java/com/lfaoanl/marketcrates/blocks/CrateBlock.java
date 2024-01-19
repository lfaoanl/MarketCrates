package com.lfaoanl.marketcrates.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends AbstractCrateBlock {
    public static final MapCodec<CrateBlock> CODEC = CrateBlock.createCodec(CrateBlock::new);

    public CrateBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    protected void openGui(BlockState state, World world, PlayerEntity player, BlockPos pos) {
        //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
        //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
        NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

        if (screenHandlerFactory != null) {
            //With this call the server will request the client to open the appropriate Screenhandler
            player.openHandledScreen(screenHandlerFactory); //openHandledScreen(screenHandlerFactory);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
}
