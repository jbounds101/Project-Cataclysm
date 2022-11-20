package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public class BulletEntityRenderer extends ProjectileEntityRenderer<BulletEntity> {

    public BulletEntityRenderer(EntityRendererFactory.Context context) { super(context); }
    @Override
    public Identifier getTexture(BulletEntity entity) {
        return new Identifier(ProjectCataclysmMod.MOD_ID, "textures/entity/bullet.png");
    }



}
