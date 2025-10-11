package com.dragofox.fennecfox.world.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import static com.dragofox.fennecfox.FennecMod.MODID;
import static com.dragofox.fennecfox.world.items.ModItems.EXAMPLE_ITEM;
import static com.dragofox.fennecfox.world.items.ModItems.FENNEC_SPAWN_EGG;


public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("fennecfox_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.fennecfox"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
                output.accept(FENNEC_SPAWN_EGG.get());
            }).build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(FENNEC_SPAWN_EGG.get());
        }
    }
}
