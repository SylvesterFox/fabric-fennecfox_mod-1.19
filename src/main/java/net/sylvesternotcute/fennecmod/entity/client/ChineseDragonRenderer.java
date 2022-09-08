package net.sylvesternotcute.fennecmod.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.ChineseDragonEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ChineseDragonRenderer extends GeoEntityRenderer<ChineseDragonEntity> {
    public ChineseDragonRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChineseDragonModel());
    }

    @Override
    public Identifier getTexture(ChineseDragonEntity chineseDragonEntity) {
        return new Identifier(FennecMod.MOD_ID, "textures/entity/dragon_chinese/texture_dragon_chinese.png");
    }
}
