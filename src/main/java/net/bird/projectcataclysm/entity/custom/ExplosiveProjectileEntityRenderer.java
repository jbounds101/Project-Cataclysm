package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class ExplosiveProjectileEntityRenderer extends ProjectileEntityRenderer<ExplosiveProjectileEntity> {

    public ExplosiveProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ExplosiveProjectileEntity entity) {
        return new Identifier(ProjectCataclysmMod.MOD_ID, "textures/entity/explosive_projectile.png");
    }
}
