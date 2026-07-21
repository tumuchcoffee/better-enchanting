package com.goofyahgames.enchantmentmod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(BetterEnchanting.MODID)
public class BetterEnchanting {
    public static final String MODID = "enchantment_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredBlock<Block> PEDESTAL = BLOCKS.registerSimpleBlock("enchanted_pedestal", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem("enchanted_pedestal", PEDESTAL);

    // Level 2 central pedestal — crafted from copper ingots, glass, and deepslate
    public static final DeferredBlock<CentralPedestalBlock> CENTRAL_PEDESTAL_2 =
            BLOCKS.registerBlock("central_pedestal_2", props -> new CentralPedestalBlock(
                    props.mapColor(MapColor.COLOR_ORANGE).noOcclusion()));
    public static final DeferredItem<BlockItem> CENTRAL_PEDESTAL_2_ITEM =
            ITEMS.registerSimpleBlockItem("central_pedestal_2", CENTRAL_PEDESTAL_2);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CentralPedestalBlockEntity>> CENTRAL_PEDESTAL_BE_TYPE =
            BLOCK_ENTITY_TYPES.register("central_pedestal_2", () ->
                    new BlockEntityType<>(CentralPedestalBlockEntity::new, CENTRAL_PEDESTAL_2.get()));

    // Crude Brush item — crafted from ink sac + stick + string
    public static final DeferredItem<Item> CRUDE_BRUSH = ITEMS.registerSimpleItem("crude_brush");

    // Stone Tablet item — crafted from lapis lazuli and stone (shapeless)
    public static final DeferredItem<Item> STONE_TABLET = ITEMS.registerSimpleItem("stone_tablet");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ENCHANTMENT_MOD_TAB = CREATIVE_MODE_TABS.register("enchantment_mod_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.enchantment_mod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> net.minecraft.world.item.Items.ENCHANTED_BOOK.getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CRUDE_BRUSH.get());
                output.accept(STONE_TABLET.get());
                output.accept(CENTRAL_PEDESTAL_2_ITEM.get());
            }).build());

    public BetterEnchanting(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(PEDESTAL_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
