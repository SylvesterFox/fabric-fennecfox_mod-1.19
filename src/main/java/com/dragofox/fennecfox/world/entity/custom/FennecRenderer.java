package com.dragofox.fennecfox.world.entity.custom;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;


public class FennecRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<FennecEntity, R>  {
//    public static final DataTicket<FennecEntity> FENNEC_ENTITY = DataTicket.create("fennec_entity", FennecEntity.class);

    public FennecRenderer(EntityRendererProvider.Context context) {
        super(context, new FennecModel());
    }


}

