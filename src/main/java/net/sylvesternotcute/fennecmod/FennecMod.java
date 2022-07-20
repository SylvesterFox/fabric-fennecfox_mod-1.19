package net.sylvesternotcute.fennecmod;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FennecMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("fennecmod");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello, This a Fennec Mod!");
    }
}
