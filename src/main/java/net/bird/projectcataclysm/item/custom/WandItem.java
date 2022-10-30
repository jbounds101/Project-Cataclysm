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
import net.minecraft.world.World;

import java.util.UUID;

public class WandItem extends Item {
    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);



    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();

    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);
    //protected static final UUID REACH_ID = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B");
    public WandItem(Settings settings, int reach) {
        super(settings);

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

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        int cooldown = 200;
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            World world = user.getWorld();
            if (!world.isClient) {
                user.sendMessage(Text.literal("You switched places with your target! Cooldown: " + cooldown/20 + " seconds"));
            }

            //user.sendMessage(Text.translatable("You switched places!"), false);
            double x_enemy = entity.getX();
            double y_enemy = entity.getY();
            double z_enemy = entity.getZ();

            double x_user = user.getX();
            double y_user = user.getY();
            double z_user = user.getZ();

            user.setPos(x_enemy, y_enemy, z_enemy);
            entity.setPos(x_user, y_user, z_user);

            user.getItemCooldownManager().set(this, cooldown);

            return super.useOnEntity(stack, user, entity, hand);
        } else if (user.getItemCooldownManager().isCoolingDown(this)) {
            World world = user.getWorld();

            if (!world.isClient) {
                float currCD = user.getItemCooldownManager().getCooldownProgress(this, 0) * (cooldown / 20);
                String stringCD = String.format("%.1f", currCD);
                user.sendMessage(Text.literal("You can use this spell in " + stringCD + " seconds!"));
            }
            return super.useOnEntity(stack, user, entity, hand);
        }

        return super.useOnEntity(stack, user, entity, hand);

    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}
