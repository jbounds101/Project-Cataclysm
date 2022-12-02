package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.bird.projectcataclysm.explosion.AlmightyExplosionSpell;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class AlmightyPush extends Item {

    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();

    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);


    static UUID uuid2 = UUID.randomUUID();
    static String uuidAsString2 = uuid2.toString();

    protected static final UUID RANGE_ID = UUID.fromString(uuidAsString2);

    public AlmightyPush(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient()) {
            AlmightyExplosionSpell explosionSpell = new AlmightyExplosionSpell(world, null, null,
                    user.getX(), user.getY(), user.getZ(), 20);
            ((PlayerEntity) user).getItemCooldownManager().set(this, (20 * 5));
            user.sendMessage(Text.literal("Cooldown: 5 seconds"));
            explosionSpell.collectBlocksAndGetEntities(user);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);

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
