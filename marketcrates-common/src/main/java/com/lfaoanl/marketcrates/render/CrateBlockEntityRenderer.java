package com.lfaoanl.marketcrates.render;

import com.lfaoanl.marketcrates.blocks.AbstractCrateBlock;
import com.lfaoanl.marketcrates.blocks.AbstractCrateBlockEntity;
import com.lfaoanl.marketcrates.blocks.states.CrateType;
import com.lfaoanl.marketcrates.common.ItemOrientation;

import org.joml.Quaternionf;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class CrateBlockEntityRenderer<H extends AbstractCrateBlockEntity> implements BlockEntityRenderer<H> {

    private final Quaternionf SOUTH = new Quaternionf().rotateXYZ(0, 180, 0);
    private final Quaternionf EAST = new Quaternionf().rotateXYZ(0, 270, 0);
    private final Quaternionf WEST = new Quaternionf().rotateXYZ(0, 90, 0);
    private final Quaternionf INCLINED = new Quaternionf().rotateXYZ(-22.5f, 0, 0);

    public CrateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(H tileEntity, float partialTicks, MatrixStack matrix, VertexConsumerProvider buffer, int light, int overlay) {
        matrix.push();

        BlockState blockState = tileEntity.getCachedState();

        applyDirection(matrix, blockState);

        if (blockState.get(AbstractCrateBlock.TYPE) == CrateType.INCLINED) {
            matrix.multiply(INCLINED);
            matrix.translate(0, 0, 0.12);
        } else {
            matrix.translate(0, 0.1f, 0);
        }

        DefaultedList<ItemOrientation> items = tileEntity.getItems();
        for (int i = 0; i < 6; i++) {

            items.get(i).render(i, matrix, buffer, light, overlay);
        }

        if (tileEntity.isDoubleCrate()) {
            matrix.push();

            matrix.translate(0, 0.5, 0);

            for (int i = 0; i < 6; i++) {

                items.get(i + 6).render(i, matrix, buffer, light, overlay);
            }

            matrix.pop();
        }

        matrix.pop();
    }

    private void applyDirection(MatrixStack matrix, BlockState blockState) {
        if (blockState.get(AbstractCrateBlock.FACING) == Direction.SOUTH) {
            matrix.translate(1, 0, 1);
            matrix.multiply(SOUTH);
        } else if (blockState.get(AbstractCrateBlock.FACING) == Direction.EAST) {
            matrix.multiply(EAST);
            matrix.translate(0, 0, -1);
        } else if (blockState.get(AbstractCrateBlock.FACING) == Direction.WEST) {
            matrix.multiply(WEST);
            matrix.translate(-1, 0, 0);
        }
    }

}
