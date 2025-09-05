package net.sylvesternotcute.fennecmod.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.sylvesternotcute.fennecmod.FennecMod;
import net.sylvesternotcute.fennecmod.entity.custom.FennecEntity;

import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


import java.util.Optional;

public class FennecRenderer extends GeoEntityRenderer<FennecEntity> {
    public FennecRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FennecModel());
    }

    @Override
    public void renderEarly(FennecEntity entity, MatrixStack poseStack, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(entity, poseStack, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            IBone head = this.modelProvider.getBone("head");
            if (head != null) {
                // Сохраняем текущую матрицу
                poseStack.push();


                // Применяем трансформации головы из модели
                // Пивот головы: [0, 8.3, -5.7] в пикселях, переводим в блоки (делим на 16)
                poseStack.translate(0.0, 8.3 / 16.0, -5.7 / 16.0);


                // Применяем повороты напрямую (быстро и отзывчиво)
                poseStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(entity.headYaw * ((float) Math.PI / 180F)));
                poseStack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(entity.headPitch * ((float) Math.PI / 180F)));
                poseStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(head.getRotationZ()));

                // Смещение к морде
                // Нос в модели: origin=[-2, 7.1, -14.7], size=[4, 2, 3]
                // Центр носа: [0, 8.1, -13.2], относительно пивота головы: [0, -0.2, -7.5]
                poseStack.translate(0.0, -0.2 / 16.0, -7.5 / 16.0);

                // Дополнительное смещение для точного позиционирования
                poseStack.translate(-0.1, -0.05, -0.2); // Немного вниз и вперед

                // Поворачиваем предмет (экспериментируйте с углами)
                poseStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
                poseStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-50));
                poseStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0));

                // Масштабируем предмет
                poseStack.scale(0.7f, 0.7f, 0.7f);

                // Рендерим предмет
                MinecraftClient.getInstance().getItemRenderer().renderItem(
                        itemStack,
                        ModelTransformation.Mode.GROUND,
                        packedLightIn,
                        OverlayTexture.DEFAULT_UV,
                        poseStack,
                        renderTypeBuffer,
                        0
                );

                poseStack.pop();
            }
        }
    }


    @Override
    public Identifier getTexture(FennecEntity entity) {
        return new Identifier(FennecMod.MOD_ID, "textures/entity/fennec/texture_fennec_fox.png");
    }
}
