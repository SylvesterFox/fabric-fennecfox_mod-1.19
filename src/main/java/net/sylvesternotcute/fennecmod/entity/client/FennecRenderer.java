package net.sylvesternotcute.fennecmod.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.FennecEntity;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


import java.util.Optional;

public class FennecRenderer extends GeoEntityRenderer<FennecEntity> {
    public FennecRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FennecModel());
    }

    @Override
    public void render(FennecEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light) {
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumer, light);
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(
                    this.getGeoModelProvider().getModel(FennecModel.MODEL).getBone("head").get().getPivotX() / 16.0F,
                    this.getGeoModelProvider().getModel(FennecModel.MODEL).getBone("head").get().getPivotY() / 16.0F,
                    this.getGeoModelProvider().getModel(FennecModel.MODEL).getBone("head").get().getPivotZ() / 16.0F);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(entity.getMaxHeadRotation()));
            matrixStack.translate(0.05999999865889549, 0.27000001072883606, -0.5);
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer, 0);
            matrixStack.pop();
        }

    }



    @Override
    public Identifier getTexture(FennecEntity entity) {
        return new Identifier(FennecMod.MOD_ID, "textures/entity/fennec/texture_fennec_fox.png");
    }
}
