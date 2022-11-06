package net.bird.projectcataclysm.entity.custom;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class MissileEntityModel extends EntityModel<MissileEntity> {
    private final ModelPart bb_main;
    public MissileEntityModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 32).cuboid(-8.0F, -22.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F))
                .uv(4, 34).cuboid(-7.0F, -38.0F, -7.0F, 14.0F, 16.0F, 14.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-8.0F, -46.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
                .uv(8, 36).cuboid(-6.0F, -78.0F, -6.0F, 12.0F, 8.0F, 12.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(-4.0F, -86.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(24, 0).cuboid(-2.0F, -94.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-8.0F, -70.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -36.0F, 7.0F, 2.0F, 22.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-1.0F, -31.0F, 11.0F, 2.0F, 20.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 26).cuboid(-1.0F, -22.0F, 15.0F, 2.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(80, 8).cuboid(-4.0F, -6.0F, -4.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(72, 4).cuboid(-6.0F, -4.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F))
                .uv(64, 0).cuboid(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-8.0F, -62.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, -22.0F, 15.0F, 2.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-1.0F, -31.0F, 11.0F, 2.0F, 20.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -36.0F, 7.0F, 2.0F, 22.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r2 = bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, -22.0F, 15.0F, 2.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-1.0F, -31.0F, 11.0F, 2.0F, 20.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -36.0F, 7.0F, 2.0F, 22.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r3 = bb_main.addChild("cube_r3", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, -22.0F, 15.0F, 2.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-1.0F, -31.0F, 11.0F, 2.0F, 20.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -36.0F, 7.0F, 2.0F, 22.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(MissileEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
