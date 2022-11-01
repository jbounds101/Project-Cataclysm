package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.UUID;

public class BlinkWand extends Item {
    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    //private final float REACH;
    //private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);
    public BlinkWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        int cooldown = 40;
        PlayerEntity user = context.getPlayer();
        assert user != null;
        ItemCooldownManager cd = user.getItemCooldownManager();
        if (!cd.isCoolingDown(this)) {

            World world = context.getWorld();

            BlockPos positionClicked = context.getBlockPos();
            int x = positionClicked.getX();
            int y = positionClicked.getY();
            int z = positionClicked.getZ();


            user.setPos(x, y + 1, z);

            cd.set(this, cooldown);
            if (!world.isClient) {
                float currCD = cd.getCooldownProgress(this, 0) * (cooldown / 20);
                String stringCD = String.format("%.0f", currCD);
                user.sendMessage(Text.literal("You blinked away! Cooldown: " + stringCD + " seconds"));
            }

            return super.useOnBlock(context);

        } else {
            user.sendMessage(Text.literal("hi"));
            World world = user.getWorld();
            // tell player the cooldown
            if (!world.isClient) {
                float currCD = cd.getCooldownProgress(this, 0) * (cooldown / 20);
                String stringCD = String.format("%.1f", currCD);
                user.sendMessage(Text.literal("You can use this spell in: " + stringCD + "seconds!2"));
            }
            return super.useOnBlock(context);
        }

        //return super.useOnBlock(context);


    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getAttributeModifiers(stack, slot);
        builder.putAll(modifiers);
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(ReachEntityAttributes.REACH,
                    new EntityAttributeModifier(REACH_ID, "reach Increase", 20,
                            EntityAttributeModifier.Operation.ADDITION));

        }
        return builder.build();
    }
}
