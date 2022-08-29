package net.sylvesternotcute.fennecmod.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.FennecEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FennecRenderer extends GeoEntityRenderer<FennecEntity> {
    public FennecRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FennecModel());
    }

    @Override
    public Identifier getTexture(FennecEntity entity) {
        return new Identifier(FennecMod.MOD_ID, "textures/entity/fennec/texture_fennec_fox.png");
    }
}
