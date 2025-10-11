package com.dragofox.fennecfox.world.entity.custom;

import com.dragofox.fennecfox.FennecMod;
import net.minecraft.resources.ResourceLocation;


import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class FennecModel extends GeoModel<FennecEntity> {
    private final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(FennecMod.MODID, "geo/fennec");
    private final ResourceLocation animations = ResourceLocation.fromNamespaceAndPath(FennecMod.MODID, "entity/fennec");

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(FennecMod.MODID, "textures/fennec/texture_fennec_fox.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FennecEntity animatable) {
        return animations;
    }

    @Override
    public void setCustomAnimations(AnimationState<FennecEntity> animationState) {
        super.setCustomAnimations(animationState);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head != null) {
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
