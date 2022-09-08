package net.sylvesternotcute.fennecmod.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.ModEntities;
import net.sylvesternotcute.fennecmod.entity.custom.FennecEntity;

public class ModRegistries {
    public static void registerModStuffs() {
        registerAttributes();
    }

    private static void registerAttributes() {
        FennecMod.LOGGER.info("Set Attributes entities..");
        FabricDefaultAttributeRegistry.register(ModEntities.FENNEC, FennecEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.CHINESE_DRAGON, FennecEntity.setAttributes());
    }
}
