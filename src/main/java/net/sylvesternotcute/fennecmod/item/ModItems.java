package net.sylvesternotcute.fennecmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.ModEntities;

public class ModItems {

    public static final Item FENNEC_SPAWN_EGG = registerItem("fennec_spawn_egg", new SpawnEggItem(ModEntities.FENNEC,  0xFFE599, 0xF6B26B,
            new FabricItemSettings().group(ModItemGroup.FOXXOGROUP)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(FennecMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FennecMod.LOGGER.info("Registering Mod Items for " + FennecMod.MOD_ID);
    }
}
