package com.lfaoanl.marketcrates.common;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import org.joml.Quaternionf;

import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

public class ItemOrientation {

    private final ItemStack itemStack;

    private final float height;
    private final Quaternionf[] rotation = new Quaternionf[3];


    public static final ItemOrientation EMPTY = new ItemOrientation(ItemStack.EMPTY);

    private static Quaternionf HORIZONTAL;

    private boolean hasOrientations = false;

    @SuppressWarnings("unchecked")
    private static final Consumer<MatrixStack>[] cratePositions = new Consumer[]{
            (Object m) -> ((MatrixStack) m).translate(0.6, 0.1, 0.65), // Upper Left
            (Object m) -> ((MatrixStack) m).translate(0.6, 0.1, 0.4), // Middle left

            (Object m) -> ((MatrixStack) m).translate(0.6, 0.1, 0.12), // Lower left
            (Object m) -> ((MatrixStack) m).translate(0.4, 0.1, 0.65), // Upper right

            (Object m) -> ((MatrixStack) m).translate(0.4, 0.1, 0.4), // Middle right
            (Object m) -> ((MatrixStack) m).translate(0.4, 0.1, 0.12) // Lower right
    };

    public ItemOrientation(ItemStack itemStack) {
        this.itemStack = itemStack;
        height = 0.1f;

//        if (MarketCrates.proxy.getWorld() != null) {
            generateOrientations();
//        }
    }

    public Runnable generateOrientations() {
        if (hasOrientations) {
            return () -> {
            };
        }

        int lowIncline = -10;
        int incline = 25;
        rotation[0] = new Quaternionf().rotateXYZ(randomInt(lowIncline, incline), randomInt(45), randomInt(incline));
        rotation[1] = new Quaternionf().rotateXYZ(randomInt(lowIncline, incline), randomInt(45), randomInt(incline));
        rotation[2] = new Quaternionf().rotateXYZ(randomInt(lowIncline, incline), randomInt(45), randomInt(incline));

        HORIZONTAL = new Quaternionf().rotateXYZ(85, 0, 0);

        hasOrientations = true;

        return () -> {
        };
    }

    public static DefaultedList<ItemOrientation> toItemOrientation(DefaultedList<ItemStack> stacks) {
        DefaultedList<ItemOrientation> list = DefaultedList.ofSize(stacks.size(), ItemOrientation.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            list.set(i, new ItemOrientation(stacks.get(i)));
        }
        return list;
    }

    public static DefaultedList<ItemStack> toItemStack(DefaultedList<ItemOrientation> stacks) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(stacks.size(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            list.set(i, stacks.get(i).itemStack);
        }
        return list;
    }

    private float randomInt(int i) {
        return randomInt(-i, i);
    }

    private float randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public void render(int index, MatrixStack matrix, VertexConsumerProvider buffer, int light, int overlay) {
        matrix.push();
        cratePositions[index].accept(matrix);

        int round = Math.max(1, Math.round(itemStack.getCount() / 21f));
        for (int i = 0; i < round; i++) {
            renderLayer(i, matrix, buffer, light, overlay);
        }

        matrix.pop();
    }

    private void renderLayer(int layer, MatrixStack matrix, VertexConsumerProvider buffer, int light, int overlay) {
        matrix.push();

        matrix.translate(0, height * layer, 0);
        matrix.multiply(rotation[layer]);

        renderItem(matrix, buffer, light, overlay);

        matrix.pop();
    }

    private void renderItem(MatrixStack matrix, VertexConsumerProvider buffer, int light, int overlay) {
        matrix.multiply(HORIZONTAL);
        matrix.scale(0.75f, 0.75f, 0.75f);
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                itemStack,
                ModelTransformationMode.GROUND,
                light,
                overlay,
                matrix,
                buffer,
                MarketCrates.proxy.getWorld(),
                0 // (LivingEntity).getId()
        );
    }

    public boolean isEmpty() {
        if (this.itemStack == ItemStack.EMPTY) {
            return true;
        } else if (this.itemStack.getItem() != Items.AIR) {
            return this.itemStack.getCount() <= 0;
        } else {
            return true;
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}


