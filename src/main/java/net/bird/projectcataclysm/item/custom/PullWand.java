package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
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
import net.minecraft.world.World;

import java.util.UUID;

public class PullWand extends Item {

    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);
    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();
    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);
    public PullWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        int cooldown = 40;
        float vec3f = user.getYaw();
        float dir = user.getHorizontalFacing().asRotation();
        float xValue = 0;
        double yValue = 0;

            if (!user.getItemCooldownManager().isCoolingDown(this)) {
                if (user.getY() > entity.getY() + 3) {
                    entity.setVelocity(entity.getVelocity().add(-0.15 * (entity.getX() - user.getX()), -0.15 * (entity.getY() - user.getY()),
                            -0.15 * (entity.getZ() - user.getZ())));
                } else {
                    entity.setVelocity(entity.getVelocity().add(-0.15 * (entity.getX() - user.getX()), 0.8,
                            -0.15 * (entity.getZ() - user.getZ())));
                }
                if (!user.getWorld().isClient) {
                    user.sendMessage(Text.literal("You pulled your target towards you! Cooldown: " + cooldown/20 + " seconds"));
                }
                user.getItemCooldownManager().set(this, cooldown);
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
