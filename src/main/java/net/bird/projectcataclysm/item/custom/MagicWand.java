package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.UUID;

public class MagicWand extends Item {
    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);

    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();
    PlayerEntity u;
    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);
    Switcher s;
    public MagicWand(Settings settings) {
        super(settings);

        s = new Switcher();
        s.list.addNode(1); // switch
        s.list.addNode(2); // wand
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {

        u = user;

        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int cooldown = 0;
        u.sendMessage(Text.literal("hi"));
        if (!u.getItemCooldownManager().isCoolingDown(this)) {
            World world = u.getWorld();
            if (!world.isClient) {
                u.sendMessage(Text.literal("You switched places with your target! Cooldown: " + cooldown/20 + " seconds"));
            }

            //user.sendMessage(Text.translatable("You switched places!"), false);
            double x_enemy = target.getX();
            double y_enemy = target.getY();
            double z_enemy = target.getZ();
            u.sendMessage(Text.literal(String.valueOf(x_enemy)));

            double x_user = u.getX();
            double y_user = u.getY();
            double z_user = u.getZ();

            u.setPos(x_enemy, y_enemy, z_enemy);
            target.setPos(x_user, y_user + 5, z_user);

            u.getItemCooldownManager().set(this, cooldown);

            return super.postHit(stack, target, attacker);
        } else if (u.getItemCooldownManager().isCoolingDown(this)) {
            World world = u.getWorld();

            if (!world.isClient) {
                float currCD = u.getItemCooldownManager().getCooldownProgress(this, 0) * (cooldown / 20);
                String stringCD = String.format("%.1f", currCD);
                u.sendMessage(Text.literal("You can use this spell in " + stringCD + " seconds!"));
            }
            return super.postHit(stack, target, attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();

        return super.useOnBlock(context);
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
