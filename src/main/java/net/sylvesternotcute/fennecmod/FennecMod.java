package net.sylvesternotcute.fennecmod;

import net.fabricmc.api.ModInitializer;
import net.sylvesternotcute.fennecmod.item.ModItems;
import net.sylvesternotcute.fennecmod.util.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class FennecMod implements ModInitializer {

    public static final String MOD_ID = "fennecmod";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Hello, This a Fennec Mod!");

        ModRegistries.registerModStuffs();
        ModItems.registerModItems();

        GeckoLib.initialize();
    }
}
