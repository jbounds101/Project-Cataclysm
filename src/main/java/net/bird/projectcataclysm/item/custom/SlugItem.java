package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.entity.custom.SlugEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SlugItem extends Item {

    public SlugItem(Item.Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createSlug(World world, ItemStack stack, LivingEntity shooter) {
        SlugEntity slugEntity = new SlugEntity(shooter, world);
        return slugEntity;
    }
 }
