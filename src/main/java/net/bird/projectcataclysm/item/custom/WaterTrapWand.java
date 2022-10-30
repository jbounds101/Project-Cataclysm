package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import java.util.UUID;

public class WaterTrapWand extends Item {

    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();

    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);

    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();

    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);
    public WaterTrapWand(Settings settings) {
        super(settings);
    }



    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        int cooldown = 300;
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            World world = user.getWorld();
            if (!world.isClient()) {
                user.sendMessage(Text.literal("You entrapped your target in a water cage! Cooldown: " + cooldown/20 + " seconds"));
            }
            // ------- FIRST LAYER ----------
            BlockPos pos1 = new BlockPos(x + 1, y, z + 1);
            user.getWorld().setBlockState(pos1, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos2 = new BlockPos(x, y, z + 1);
            user.getWorld().setBlockState(pos2, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos3 = new BlockPos(x + 1, y, z);
            user.getWorld().setBlockState(pos3, Blocks.PRISMARINE_BRICKS.getDefaultState());

            BlockPos pos4 = new BlockPos(x - 1, y, z - 1);
            user.getWorld().setBlockState(pos4, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos5 = new BlockPos(x, y, z - 1);
            user.getWorld().setBlockState(pos5, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos6 = new BlockPos(x - 1, y, z);
            user.getWorld().setBlockState(pos6, Blocks.PRISMARINE_BRICKS.getDefaultState());

            BlockPos pos7 = new BlockPos(x - 1, y, z + 1);
            user.getWorld().setBlockState(pos7, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos8 = new BlockPos(x + 1, y, z - 1);
            user.getWorld().setBlockState(pos8, Blocks.PRISMARINE_BRICKS.getDefaultState());
            // --------- SECOND LAYER -----------
            BlockPos pos9 = new BlockPos(x + 1, y + 1, z + 1);
            user.getWorld().setBlockState(pos9, Blocks.IRON_BARS.getDefaultState());
            BlockPos pos10 = new BlockPos(x, y + 1, z + 1);
            user.getWorld().setBlockState(pos10, Blocks.COBWEB.getDefaultState());
            BlockPos pos11 = new BlockPos(x + 1, y + 1, z);
            user.getWorld().setBlockState(pos11, Blocks.COBWEB.getDefaultState());

            BlockPos pos12 = new BlockPos(x - 1, y + 1, z - 1);
            user.getWorld().setBlockState(pos12, Blocks.IRON_BARS.getDefaultState());
            BlockPos pos13 = new BlockPos(x, y + 1, z - 1);
            user.getWorld().setBlockState(pos13, Blocks.COBWEB.getDefaultState());
            BlockPos pos14 = new BlockPos(x - 1, y + 1, z);
            user.getWorld().setBlockState(pos14, Blocks.COBWEB.getDefaultState());

            BlockPos pos15 = new BlockPos(x - 1, y + 1, z + 1);
            user.getWorld().setBlockState(pos15, Blocks.IRON_BARS.getDefaultState());
            BlockPos pos16 = new BlockPos(x + 1, y + 1, z - 1);
            user.getWorld().setBlockState(pos16, Blocks.IRON_BARS.getDefaultState());
            // ------ THIRD LAYER --------
            BlockPos pos17 = new BlockPos(x + 1, y + 2, z + 1);
            user.getWorld().setBlockState(pos17, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos18 = new BlockPos(x, y + 2, z + 1);
            user.getWorld().setBlockState(pos18, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos19 = new BlockPos(x + 1, y + 2, z);
            user.getWorld().setBlockState(pos19, Blocks.PRISMARINE_BRICKS.getDefaultState());

            BlockPos pos20 = new BlockPos(x - 1, y + 2, z - 1);
            user.getWorld().setBlockState(pos20, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos21 = new BlockPos(x, y + 2, z - 1);
            user.getWorld().setBlockState(pos21, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos22 = new BlockPos(x - 1, y + 2, z);
            user.getWorld().setBlockState(pos22, Blocks.PRISMARINE_BRICKS.getDefaultState());

            BlockPos pos23 = new BlockPos(x - 1, y + 2, z + 1);
            user.getWorld().setBlockState(pos23, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos24 = new BlockPos(x + 1, y + 2, z - 1);
            user.getWorld().setBlockState(pos24, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos water = new BlockPos(x, y + 2, z);
            user.getWorld().setBlockState(water, Blocks.WATER.getDefaultState());

            // -------- FOURTH LAYER -------
            BlockPos pos25 = new BlockPos(x + 1, y + 3, z + 1);
            //user.getWorld().setBlockState(pos25, Blocks.COBWEB.getDefaultState());
            BlockPos pos26 = new BlockPos(x, y + 3, z + 1);
            user.getWorld().setBlockState(pos26, Blocks.COBWEB.getDefaultState());
            BlockPos pos27 = new BlockPos(x + 1, y + 3, z);
            user.getWorld().setBlockState(pos27, Blocks.COBWEB.getDefaultState());

            BlockPos pos28 = new BlockPos(x - 1, y + 3, z - 1);
            //user.getWorld().setBlockState(pos28, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos29 = new BlockPos(x, y + 3, z - 1);
            user.getWorld().setBlockState(pos29, Blocks.COBWEB.getDefaultState());
            BlockPos pos30 = new BlockPos(x - 1, y + 3, z);
            user.getWorld().setBlockState(pos30, Blocks.COBWEB.getDefaultState());

            BlockPos pos31 = new BlockPos(x - 1, y + 3, z + 1);
            //user.getWorld().setBlockState(pos31, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos pos32 = new BlockPos(x + 1, y + 3, z - 1);
            //user.getWorld().setBlockState(pos32, Blocks.PRISMARINE_BRICKS.getDefaultState());
            BlockPos top = new BlockPos(x, y + 3, z);
            user.getWorld().setBlockState(top, Blocks.COBWEB.getDefaultState());

            user.getItemCooldownManager().set(this, 300);
        } else if (user.getItemCooldownManager().isCoolingDown(this)) {
            World world = user.getWorld();
            // tell player the cooldown
            if (!world.isClient) {
                float currCD = user.getItemCooldownManager().getCooldownProgress(this, 0) * (cooldown / 20);
                String stringCD = String.format("%.1f", currCD);
                user.sendMessage(Text.literal("You can use this spell in " + stringCD + "seconds!"));

            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getAttributeModifiers(stack, slot);
        builder.putAll(modifiers);
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(ReachEntityAttributes.REACH,
                    //creative mode reach
                    new EntityAttributeModifier(REACH_ID, "reach Increase", 20,
                            EntityAttributeModifier.Operation.ADDITION));
            //survival mode reach
            builder.put(ReachEntityAttributes.ATTACK_RANGE,
                    new EntityAttributeModifier(RANGE_ID, "range Increase", 20,
                            EntityAttributeModifier.Operation.ADDITION));


        }
        return builder.build();
    }
}
