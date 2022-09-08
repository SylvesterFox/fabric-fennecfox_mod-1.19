package net.sylvesternotcute.fennecmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.sylvesternotcute.fennecmod.entity.ModEntities;
import net.sylvesternotcute.fennecmod.entity.client.ChineseDragonRenderer;
import net.sylvesternotcute.fennecmod.entity.client.FennecRenderer;

public class FennecClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FennecMod.LOGGER.info("Client mod Initialize..");
        EntityRendererRegistry.register(ModEntities.FENNEC, FennecRenderer::new);
        EntityRendererRegistry.register(ModEntities.CHINESE_DRAGON, ChineseDragonRenderer::new);
    }
}
