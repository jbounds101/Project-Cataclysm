package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.entity.BulletEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BulletItem extends Item {
    public BulletItem(Item.Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createBullet(World world, ItemStack stack, LivingEntity shooter) {
        ArrowEntity arrowEntity = new ArrowEntity(world, shooter);
        arrowEntity.initFromStack(stack);
        return arrowEntity;
    }
}
