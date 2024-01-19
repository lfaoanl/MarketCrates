package com.lfaoanl.marketcrates.blocks.states;

import java.util.Arrays;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.shape.VoxelShape;

public enum CrateType implements StringIdentifiable {

    DEFAULT("default", 8d, false),
    INCLINED("inclined", 14d, true),
    DOUBLE("double", 16d, true);

    private final String name;
    private final double height;
    private final boolean resourceName;

    /*
    shapes.put("default", Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 8.0D, 16.0D));
    shapes.put("inclined", Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 14.0D, 16.0D));
    shapes.put("default_horizontal", Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, 8.0D, 14.0D));
    shapes.put("inclined_horizontal", Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D));
     */
    CrateType(String name, double height, boolean resourceName) {
        this.name = name;
        this.height = height;
        this.resourceName = resourceName;
    }

    public static Collection<CrateType> allValues() {
        return Arrays.asList(DEFAULT, INCLINED, DOUBLE);
    }

    @Override
    public String asString() {
        return this.name;
    }

    public VoxelShape getShape(boolean horizontal) {
        if (horizontal) {
            return Block.createCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, height, 14.0D);
        }
        return Block.createCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, height, 16.0D);
    }

    public boolean isDouble() {
        return this == DOUBLE;
    }

    public CrateType opposite() {
        return this == DEFAULT ? INCLINED : DEFAULT;
    }

    /**
     * Used for forge data generation
     * @return
     */
    public String getResource() {

        return "crate" + (resourceName ? "_" + this.name : "");
    }

    public static CrateType[] all() {
        return new CrateType[]{DEFAULT, DOUBLE, INCLINED};
    }
}
