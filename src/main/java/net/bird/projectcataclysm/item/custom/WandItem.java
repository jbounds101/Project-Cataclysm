package net.bird.projectcataclysm.item.custom;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes.REACH;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.UUID;

public class WandItem extends Item {
    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    private final float REACH;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);
    public WandItem(Settings settings, float reach) {
        super(settings);
        this.REACH = reach;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_ID, "Range modifier", this.REACH, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {


                /*
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier",
                (double)this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier",
                (double)attackSpeed, EntityAttributeModifier.Operation.ADDITION));
                */
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


}
