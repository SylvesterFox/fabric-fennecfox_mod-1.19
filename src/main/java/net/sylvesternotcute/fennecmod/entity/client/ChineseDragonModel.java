package net.sylvesternotcute.fennecmod.entity.client;

import net.minecraft.util.Identifier;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.ChineseDragonEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChineseDragonModel extends AnimatedGeoModel<ChineseDragonEntity> {
    @Override
    public Identifier getModelResource(ChineseDragonEntity object) {
        return new Identifier(FennecMod.MOD_ID, "geo/dragon_chinese.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChineseDragonEntity object) {
        return new Identifier(FennecMod.MOD_ID, "textures/entity/dragon_chinese/texture_dragon_chinese.png");
    }

    @Override
    public Identifier getAnimationResource(ChineseDragonEntity animatable) {
        return null;
    }
}
