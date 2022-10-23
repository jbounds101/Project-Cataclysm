package net.bird.projectcataclysm.entity.custom;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class SlugEntityRenderer extends ProjectileEntityRenderer<SlugEntity> {

    public SlugEntityRenderer(EntityRendererFactory.Context context) { super(context); }

    @Override
    public Identifier getTexture(SlugEntity entity) {
        return new Identifier("src/main/resources/assets/projectcataclysm/textures/item/slug.png");
    }



}
