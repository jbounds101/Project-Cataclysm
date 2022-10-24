package net.bird.projectcataclysm.item.custom;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import java.util.UUID;

public class WandItem extends Item {
    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    //private final float REACH;
    //private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);



    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();

    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);
    //protected static final UUID REACH_ID = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B");
    public WandItem(Settings settings, int reach) {
        super(settings);
        /*
        this.REACH = reach;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

        this.attributeModifiers = ImmutableMultimap.of(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_ID,
                "Reach modifier", this.REACH, EntityAttributeModifier.Operation.ADDITION));*/

        /*
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_ID, "Reach modifier", this.REACH, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();*/

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
    /*
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {

        var modifiers = super.getAttributeModifiers(stack, slot);
        modifiers.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_ID, "reach", 0, EntityAttributeModifier.Operation.ADDITION));
        return super.getAttributeModifiers(stack, slot);
        //return modifiers;
    }*/

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
                /*
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier",
                (double)this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier",
                (double)attackSpeed, EntityAttributeModifier.Operation.ADDITION));
                */
        user.sendMessage(Text.literal("You switched places with your target!"));
        //user.sendMessage(Text.translatable("You switched places!"), false);
        double x_enemy = entity.getX();
        double y_enemy = entity.getY();
        double z_enemy = entity.getZ();

        double x_user = user.getX();
        double y_user = user.getY();
        double z_user = user.getZ();

        user.setPos(x_enemy, y_enemy, z_enemy);
        entity.setPos(x_user, y_user, z_user);



        return super.useOnEntity(stack, user, entity, hand);

    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}
