package com.dragofox.fennecfox.world.entity.custom;

import com.dragofox.fennecfox.FennecMod;
import net.minecraft.resources.ResourceLocation;


import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class FennecModel extends GeoModel<FennecEntity> {
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(FennecMod.MODID, "geo/fennec.geo.json");

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(FennecMod.MODID, "textures/entity/fennec/texture_fennec.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FennecEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(FennecMod.MODID, "animations/fennec_animation.json");
    }

    @Override
    public void setCustomAnimations(AnimationState<FennecEntity> animationState) {
        super.setCustomAnimations(animationState);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        FennecEntity entity = animationState.getAnimatable();
        if (head != null) {
            // Используем новые DataTickets
            Float headPitch = animationState.getData(DataTickets.ENTITY_PITCH);
            Float headYaw = animationState.getData(DataTickets.ENTITY_YAW);
            if (headPitch != null) {
                head.setRotX(headPitch * ((float) Math.PI / 180F));
            }
            if (headYaw != null) {
                head.setRotY(headYaw * ((float) Math.PI / 180F));
            }

        }
    }
}
