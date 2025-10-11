package com.dragofox.fennecfox;

import com.dragofox.fennecfox.world.entity.MobEntites;
import com.dragofox.fennecfox.world.entity.custom.FennecEntity;
import com.dragofox.fennecfox.world.entity.custom.FennecRenderer;
import com.dragofox.fennecfox.world.items.ModCreativeTab;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.dragofox.fennecfox.world.entity.MobEntites.ENTITIES;
import static com.dragofox.fennecfox.world.items.ModCreativeTab.CREATIVE_MODE_TABS;
import static com.dragofox.fennecfox.world.items.ModItems.BLOCKS;
import static com.dragofox.fennecfox.world.items.ModItems.ITEMS;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(FennecMod.MODID)
public class FennecMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "fennecfox";
    public static final Logger LOGGER = LogUtils.getLogger();


    public FennecMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (fennecfox) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(this::onRegisterRenderers);
        // Register the item to a creative tab
        modEventBus.addListener(ModCreativeTab::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }


    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MobEntites.FENNEC_ENTITY.get(), FennecEntity.createAttributes().build());
    }

    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MobEntites.FENNEC_ENTITY.get(), FennecRenderer::new);
    }




    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
