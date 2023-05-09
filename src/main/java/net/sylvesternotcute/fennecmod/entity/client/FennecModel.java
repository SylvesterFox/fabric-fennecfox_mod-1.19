package net.sylvesternotcute.fennecmod.entity.client;

import net.minecraft.util.Identifier;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.FennecEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class FennecModel extends AnimatedGeoModel<FennecEntity> {
    public static final Identifier MODEL = new Identifier(FennecMod.MOD_ID, "geo/fennec.geo.json");
    @Override
    public Identifier getModelResource(FennecEntity object) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(FennecEntity object) {
        return new Identifier(FennecMod.MOD_ID, "textures/entity/fennec/texture_fennec_fox.png");
    }

    @Override
    public Identifier getAnimationResource(FennecEntity animatable) {
        return new Identifier(FennecMod.MOD_ID, "animations/fennec_animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(FennecEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
