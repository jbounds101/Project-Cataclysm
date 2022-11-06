package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.ProjectCataclysmModClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class MissileEntityRenderer extends EntityRenderer<MissileEntity> {
    private final MissileEntityModel model;
    public MissileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.4f;
        model = createModel(context);
    }

    private MissileEntityModel createModel(EntityRendererFactory.Context ctx) {
        return new MissileEntityModel(ctx.getPart(ProjectCataclysmModClient.MODEL_MISSILE_LAYER));
    }

    @Override
    public Identifier getTexture(MissileEntity entity) {
        return new Identifier(ProjectCataclysmMod.MOD_ID, "textures/entity/missile.png");
    }

    public void render(MissileEntity missileEntity, float y, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float pitch = MathHelper.lerp(tickDelta, missileEntity.prevPitch, missileEntity.getPitch());
        float yaw = MathHelper.lerp(tickDelta, missileEntity.prevYaw, missileEntity.getYaw());
        matrices.push();
        matrices.translate(0.0, 0.375, 0.0);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw - 90.0F));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(pitch + 90.0F));
        /*
        matrices.scale(-1.0f, -1.0f, 1.0f);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));*/
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(getTexture(missileEntity)));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.pop();
        super.render(missileEntity, y, tickDelta, matrices, vertexConsumers, light);
    }
}
