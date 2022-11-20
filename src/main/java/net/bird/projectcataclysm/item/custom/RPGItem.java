package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.entity.custom.ExplosiveProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RPGItem extends Item {
    public RPGItem(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createRPG(World world, ItemStack itemStack, LivingEntity shooter) {
        ExplosiveProjectileEntity explosiveProjectile = new ExplosiveProjectileEntity(shooter, world);
        return explosiveProjectile;
    }
}
