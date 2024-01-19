package com.lfaoanl.marketcrates.core;

import com.lfaoanl.marketcrates.Ref;
import com.lfaoanl.marketcrates.blocks.AbstractCrateBlock;
import com.lfaoanl.marketcrates.blocks.AbstractCrateBlockEntity;
import com.lfaoanl.marketcrates.blocks.CrateBlock;
import com.lfaoanl.marketcrates.blocks.CrateBlockEntity;
import com.lfaoanl.marketcrates.common.MarketCrates;
import com.lfaoanl.marketcrates.gui.CrateContainer;
import com.lfaoanl.marketcrates.gui.CrateDoubleContainer;
import com.lfaoanl.marketcrates.items.CrateItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import java.util.HashMap;

public class CrateRegistry {
    public static final Identifier CRATE_CHANNEL = new Identifier(Ref.MODID, "crate_channel");

//    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MODID);
//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);
//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);
//    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.MODID);
//
//
//    public static final RegistryObject<MenuType<CrateContainer>> CONTAINER_CRATE = CONTAINERS.register("crate", () -> IForgeContainerType.create((windowId, inv, data) -> new CrateContainer(windowId, inv)));
//    public static final RegistryObject<MenuType<CrateDoubleContainer>> CONTAINER_CRATE_DOUBLE = CONTAINERS.register("crate_double", () -> IForgeContainerType.create((windowId, inv, data) -> new CrateDoubleContainer(windowId, inv)));


    public static String[] woodTypes = new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson", "warped"};

//    public static final RegistryObject<BlockEntityType<CrateTileEntity>> CRATE_TILE = TILES.register("crate", () -> BlockEntityType.Builder.of(CrateTileEntity::new, CrateRegistry.validCrates()).build(null));

    public static final HashMap<String, Item> items = new HashMap<>();
    public static final HashMap<String, Block> blocks = new HashMap<>();

    public static BlockEntityType<CrateBlockEntity> CRATE_BLOCK_ENTITY;

    public static ScreenHandlerType<CrateDoubleContainer> CRATE_DOUBLE_SCREEN;
    public static ScreenHandlerType<CrateContainer> CRATE_SCREEN;

    public static void preInit() {
        for (String type : woodTypes) {
            AbstractCrateBlock block = new CrateBlock(AbstractCrateBlock.properties);
            blocks.put(type, block);
            items.put(type, new CrateItem(block, new FabricItemSettings()));
        }

        CRATE_DOUBLE_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(Ref.MODID, "crate_double"), CrateDoubleContainer::new);
        CRATE_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(Ref.MODID, "crate"), CrateContainer::new);

    }

    public static void init() {

        for (String type : woodTypes) {
            Identifier resourceLocation = new Identifier(Ref.MODID, type + "_crate");
            Registry.register(Registries.BLOCK, resourceLocation, blocks.get(type));
            Registry.register(Registries.ITEM, resourceLocation, items.get(type));
        }

        Identifier rlCrateBlockEntity = new Identifier(Ref.MODID, "crate");

        Block[] blocksArray = blocks.values().toArray(new Block[blocks.size()]);

        BlockEntityType<CrateBlockEntity> beBuilder = FabricBlockEntityTypeBuilder.create(CrateBlockEntity::new, blocksArray).build(null);
        CRATE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, rlCrateBlockEntity, beBuilder);
    }

}
