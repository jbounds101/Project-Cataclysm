package net.bird.projectcataclysm.item.custom;

import net.bird.projectcataclysm.item.ModItems;
import net.bird.projectcataclysm.util.InventoryUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SpellSwitcher extends Item {

    enum Spell {
        NONE,
        WALL,
        LIFESWAP,
        SWITCH,
        WEBTRAP,
        BLINK,
        WATERTRAP
    }

    public static Spell currentSpell = Spell.NONE;
    public static Node currentNode;


    public CircularLinkedList spellList = new CircularLinkedList();

    class CircularLinkedList {
        private Node head = null;
        private Node tail = null;


        public Node addNode(Item spell, Spell spellEnum) {
            Node newNode = new Node(spell, spellEnum);

            if (head == null) {
                head = newNode;
            } else {
                tail.nextNode = newNode;
            }

            tail = newNode;
            tail.nextNode = head;
            return newNode;
        }
    }
    class Node {
        Item spell;
        boolean inInventory;
        Spell spellEnum;
        Node nextNode;

        public Node(Item spell, Spell spellEnum) {
            this.spell = spell;
            this.inInventory = false;
            this.spellEnum = spellEnum;
        }
    }
    public SpellSwitcher(Settings settings) {
        super(settings);
        spellList.addNode(null, Spell.NONE);
        spellList.addNode(ModItems.WALL_WAND, Spell.WALL);
        spellList.addNode(ModItems.WAND_LIFE_SWAP, Spell.LIFESWAP);
        spellList.addNode(ModItems.WAND, Spell.SWITCH);
        spellList.addNode(ModItems.WATER_TRAP_WAND, Spell.WATERTRAP);
        currentNode = spellList.head;

    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient()) {
            Node head = spellList.head;
            Node mover = spellList.head.nextNode;
            int hasSpell = 0;
            while (mover != head) {
                if (InventoryUtil.hasPlayerStackInInventory((PlayerEntity) user, mover.spell)) {
                    mover.inInventory = true;
                    hasSpell = 1;
                }
                mover = mover.nextNode;
            }
            mover = currentNode.nextNode;
            // checks if player has any spells
            if (hasSpell != 0) {
                while (mover != currentNode) {
                    if (mover.inInventory) {
                        break;
                    } else {
                        mover = mover.nextNode;
                    }
                }
                currentNode = mover;
                currentSpell = mover.spellEnum;
            } else {
                user.sendMessage(Text.literal("No Spell Found in Inventory!"));
                currentSpell = Spell.NONE;
            }
            user.sendMessage(Text.literal("Current Spell: " + currentSpell));
            super.onStoppedUsing(stack, world, user, remainingUseTicks);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);

    }

    public Enum<Spell> getCurrentSpell() {
        return currentSpell;
    }





}
