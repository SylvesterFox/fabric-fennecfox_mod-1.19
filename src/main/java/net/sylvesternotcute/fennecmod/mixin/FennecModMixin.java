package net.sylvesternotcute.fennecmod.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.sylvesternotcute.fennecmod.FennecMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TitleScreen.class)
public class FennecModMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        FennecMod.LOGGER.info("This mixin for Fennec Mod.");
    }
}
