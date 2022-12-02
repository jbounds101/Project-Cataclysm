package net.bird.projectcataclysm.entity.custom;

import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.item.custom.PortalGunItem;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class PortalProjectileRenderer extends ProjectileEntityRenderer {
    public PortalProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        if (PortalGunItem.getCurrentPortal() == PortalGunItem.BLUE) {
            return new Identifier(ProjectCataclysmMod.MOD_ID, "textures/entity/blue_portal_projectile.png");
        }
        else {
            return new Identifier(ProjectCataclysmMod.MOD_ID, "textures/entity/orange_portal_projectile.png");
        }
    }
}
