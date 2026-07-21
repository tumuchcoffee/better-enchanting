package com.goofyahgames.enchantmentmod;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BetterEnchanting.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = BetterEnchanting.MODID, value = Dist.CLIENT)
public class BetterEnchantingClient {
    public BetterEnchantingClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        BetterEnchanting.LOGGER.info("HELLO FROM CLIENT SETUP");
        BetterEnchanting.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                BetterEnchanting.CENTRAL_PEDESTAL_BE_TYPE.get(),
                CentralPedestalBlockEntityRenderer::new
        );
    }
}
