package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class WebWand extends Item {
    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();

    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);


    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();

    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);

    public WebWand(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        int cooldown = 200;
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            World world = user.getWorld();
            if (!world.isClient) {
                user.sendMessage(Text.literal("You entrapped your target in webs! Cooldown: " + cooldown/20 + " seconds"));
            }

            BlockPos pos1 = new BlockPos(x+1, y, z+1);
            user.getWorld().setBlockState(pos1, Blocks.COBWEB.getDefaultState());
            BlockPos pos2 = new BlockPos(x, y, z + 1);
            user.getWorld().setBlockState(pos2, Blocks.COBWEB.getDefaultState());
            BlockPos pos3 = new BlockPos(x+1, y, z);
            user.getWorld().setBlockState(pos3, Blocks.COBWEB.getDefaultState());

            BlockPos pos4 = new BlockPos(x-1, y, z-1);
            user.getWorld().setBlockState(pos4, Blocks.COBWEB.getDefaultState());
            BlockPos pos5 = new BlockPos(x, y, z - 1);
            user.getWorld().setBlockState(pos5, Blocks.COBWEB.getDefaultState());
            BlockPos pos6 = new BlockPos(x-1, y, z);
            user.getWorld().setBlockState(pos6, Blocks.COBWEB.getDefaultState());

            BlockPos pos7 = new BlockPos(x-1, y, z+1);
            user.getWorld().setBlockState(pos7, Blocks.COBWEB.getDefaultState());
            BlockPos pos8 = new BlockPos(x+1, y, z - 1);
            user.getWorld().setBlockState(pos8, Blocks.COBWEB.getDefaultState());

            BlockPos pos9 = new BlockPos(x+1, y+2, z+1);
            user.getWorld().setBlockState(pos9, Blocks.COBWEB.getDefaultState());
            BlockPos pos10 = new BlockPos(x, y+2, z + 1);
            user.getWorld().setBlockState(pos10, Blocks.COBWEB.getDefaultState());
            BlockPos pos11 = new BlockPos(x+1, y+2, z);
            user.getWorld().setBlockState(pos11, Blocks.COBWEB.getDefaultState());

            BlockPos pos12 = new BlockPos(x-1, y+2, z-1);
            user.getWorld().setBlockState(pos12, Blocks.COBWEB.getDefaultState());
            BlockPos pos13 = new BlockPos(x, y+2, z - 1);
            user.getWorld().setBlockState(pos13, Blocks.COBWEB.getDefaultState());
            BlockPos pos14 = new BlockPos(x-1, y+2, z);
            user.getWorld().setBlockState(pos14, Blocks.COBWEB.getDefaultState());

            BlockPos pos15 = new BlockPos(x-1, y+2, z+1);
            user.getWorld().setBlockState(pos15, Blocks.COBWEB.getDefaultState());
            BlockPos pos16 = new BlockPos(x+1, y+2, z - 1);
            user.getWorld().setBlockState(pos16, Blocks.COBWEB.getDefaultState());


            user.getItemCooldownManager().set(this, cooldown);
            return super.useOnEntity(stack, user, entity, hand);

        } else if (user.getItemCooldownManager().isCoolingDown(this)) {
            World world = user.getWorld();
            // tell player the cooldown
            if (!world.isClient) {
                float currCD = user.getItemCooldownManager().getCooldownProgress(this, 0) * (cooldown / 20);
                String stringCD = String.format("%.1f", currCD);
                user.sendMessage(Text.literal("You can use this spell in " + stringCD + " seconds!"));
            }
            return super.useOnEntity(stack, user, entity, hand);
        }
        //user.getItemCooldownManager().set(this, 200);
        return super.useOnEntity(stack, user, entity, hand);


    }
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getAttributeModifiers(stack, slot);
        builder.putAll(modifiers);
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(ReachEntityAttributes.REACH,
                    new EntityAttributeModifier(REACH_ID, "reach Increase", 20,
                            EntityAttributeModifier.Operation.ADDITION));
            builder.put(ReachEntityAttributes.ATTACK_RANGE,
                    new EntityAttributeModifier(RANGE_ID, "range Increase", 20,
                            EntityAttributeModifier.Operation.ADDITION));

        }
        return builder.build();
    }


}
