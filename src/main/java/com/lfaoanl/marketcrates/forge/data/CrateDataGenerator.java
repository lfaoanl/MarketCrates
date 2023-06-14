package com.lfaoanl.marketcrates.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

public class CrateDataGenerator {

    public static void init(GatherDataEvent event) {


        DataGenerator generator = event.getGenerator();
        // Recipes
        generator.addProvider(event.includeServer(), new CrateRecipeProvider(generator));
        // LootTable
        generator.addProvider(event.includeServer(), new CrateLootTableProvider(generator));


        // BlockStates
        // System.out.println("LFAOANL: Data generator");
        generator.addProvider(event.includeClient(), new CrateBlockStates(generator, event.getExistingFileHelper()));
        // Items
        generator.addProvider(event.includeClient(), new CrateItemModelProvider(generator, event.getExistingFileHelper()));

    }
}
