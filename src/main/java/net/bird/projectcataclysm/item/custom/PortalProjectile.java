package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.entity.custom.PortalProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PortalProjectile extends Item {
    public PortalProjectile(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createPortalProjectile(World world, ItemStack itemStack, LivingEntity shooter) {
        PortalProjectileEntity portalProjectile = new PortalProjectileEntity(shooter, world);
        return portalProjectile;
    }
}
