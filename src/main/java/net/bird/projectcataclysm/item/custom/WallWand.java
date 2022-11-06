package net.bird.projectcataclysm.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class WallWand extends Item {

    static UUID uuid = UUID.randomUUID();
    static String uuidAsString = uuid.toString();
    //private final float REACH;
    //private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    protected static final UUID REACH_ID = UUID.fromString(uuidAsString);
    public WallWand(Settings settings) {
        super(settings);
    }
    boolean isStone;
    boolean isCobble;
    boolean isGravel;
    boolean isSand;
    boolean isNear;
    public ActionResult useOnBlock(ItemUsageContext context) {
        int cooldown = 40;
        isStone = false;
        isCobble = false;
        isGravel = false;
        isSand = false;
        isNear = false;
        PlayerEntity user = context.getPlayer();
        World world = context.getWorld();

        BlockPos userPos = user.getBlockPos();
        int userX = userPos.getX();
        int userY = userPos.getY();
        int userZ = userPos.getZ();
        BlockPos positionClicked = context.getBlockPos();
        //user.sendMessage(Text.translatable("user y: " + String.valueOf(userY)), false);
        int y = positionClicked.getY() + 1;

        if (context.getWorld().getBlockState(positionClicked).equals(Blocks.GRASS.getDefaultState()) ||
                context.getWorld().getBlockState(positionClicked).equals(Blocks.TALL_GRASS.getDefaultState()) ||
                context.getWorld().getBlockState(positionClicked).equals(Blocks.SUNFLOWER.getDefaultState()) ||
                context.getWorld().getBlockState(positionClicked).equals(Blocks.CORNFLOWER.getDefaultState()) ||
                context.getWorld().getBlockState(positionClicked).equals(Blocks.MANGROVE_PROPAGULE.getDefaultState()) ||
                context.getWorld().getBlockState(positionClicked).equals(Blocks.SNOW.getDefaultState())) {
            y = positionClicked.getY();
        }

        //user.sendMessage(Text.translatable("position y: " + String.valueOf(y)), false);

        if (positionClicked.getX() == userX && positionClicked.getZ() == userZ && y == userY)  {
            isNear = true;
        }
        if (context.getWorld().getBlockState(positionClicked).equals(Blocks.STONE.getDefaultState())) {
            isStone = true;
        } else if (context.getWorld().getBlockState(positionClicked).equals(Blocks.COBBLESTONE.getDefaultState())) {
            isCobble = true;
        } else if (context.getWorld().getBlockState(positionClicked).equals(Blocks.GRAVEL.getDefaultState())) {
            isGravel = true;
        } else if (context.getWorld().getBlockState(positionClicked).equals(Blocks.SAND.getDefaultState())) {
            isSand = true;
        }
        int x = positionClicked.getX();


        int z = positionClicked.getZ();
        BlockPos positionClicked2 = new BlockPos(x, y, z);
        //positionClicked.add(100, 100, 100);
        float dir = context.getPlayerFacing().asRotation();

        //user.sendMessage(Text.literal(String.valueOf(dir)));
        if (dir == 0 || dir == 180) {
            for (int x2 = 0; x2 < 3; x2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x + x2, y + y2, z);
                    if (!isNear) {
                        if (isStone) {
                            context.getWorld().setBlockState(positionClicked, Blocks.STONE.getDefaultState());
                        } else if (isCobble) {
                            context.getWorld().setBlockState(positionClicked, Blocks.COBBLESTONE.getDefaultState());
                        } else if (isGravel) {
                            context.getWorld().setBlockState(positionClicked, Blocks.GRAVEL.getDefaultState());
                        } else if (isSand) {
                            context.getWorld().setBlockState(positionClicked, Blocks.SAND.getDefaultState());
                        } else {
                            context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                        }
                    }

                }

            }
            for (int x2 = -3; x2 <= 0; x2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x + x2, y + y2, z);
                    if (!isNear) {
                        if (isStone) {
                            context.getWorld().setBlockState(positionClicked, Blocks.STONE.getDefaultState());
                        } else if (isCobble) {
                            context.getWorld().setBlockState(positionClicked, Blocks.COBBLESTONE.getDefaultState());
                        } else if (isGravel) {
                            context.getWorld().setBlockState(positionClicked, Blocks.GRAVEL.getDefaultState());
                        } else if (isSand) {
                            context.getWorld().setBlockState(positionClicked, Blocks.SAND.getDefaultState());
                        } else {
                            context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                        }
                    }
                }
            }
        } else if (dir == 270 || dir == 90) {
            for (int z2 = 0; z2 < 3; z2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x, y + y2, z + z2);
                    if (!isNear) {
                        if (isStone) {
                            context.getWorld().setBlockState(positionClicked, Blocks.STONE.getDefaultState());
                        } else if (isCobble) {
                            context.getWorld().setBlockState(positionClicked, Blocks.COBBLESTONE.getDefaultState());
                        } else if (isGravel) {
                            context.getWorld().setBlockState(positionClicked, Blocks.GRAVEL.getDefaultState());
                        } else if (isSand) {
                            context.getWorld().setBlockState(positionClicked, Blocks.SAND.getDefaultState());
                        } else {
                            context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                        }
                    }
                }

            }
            for (int z2 = -3; z2 <= 0; z2++) {
                for (int y2 = 0; y2 < 5; y2++) {
                    positionClicked = new BlockPos(x, y + y2, z + z2);
                    if (!isNear) {
                        if (isStone) {
                            context.getWorld().setBlockState(positionClicked, Blocks.STONE.getDefaultState());
                        } else if (isCobble) {
                            context.getWorld().setBlockState(positionClicked, Blocks.COBBLESTONE.getDefaultState());
                        } else if (isGravel) {
                            context.getWorld().setBlockState(positionClicked, Blocks.GRAVEL.getDefaultState());
                        } else if (isSand) {
                            context.getWorld().setBlockState(positionClicked, Blocks.SAND.getDefaultState());
                        } else {
                            context.getWorld().setBlockState(positionClicked, Blocks.PACKED_MUD.getDefaultState());
                        }
                    }
                }
            }

        }
        //cooldown
        assert user != null;
        user.getItemCooldownManager().set(this, cooldown);
        if (!world.isClient) {
            float currCD = user.getItemCooldownManager().getCooldownProgress(this, 0) * (cooldown / 20);
            String stringCD = String.format("%.0f", currCD);
            user.sendMessage(Text.literal("You summoned a wall! Cooldown: " + stringCD + " seconds"));
        }
        return ActionResult.PASS;
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
