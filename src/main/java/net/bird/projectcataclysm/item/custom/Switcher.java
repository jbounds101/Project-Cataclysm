package net.bird.projectcataclysm.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Switcher {
    Node currSpell;
    CircularLinkedList list;
    public Switcher() {
        this.list = new CircularLinkedList();
        currSpell = list.addNode(1); // switch
        list.addNode(2); // web

    }
    public int getCurrentSpell() {
        return currSpell.value;
    }


    public class CircularLinkedList {
        private Node head = null;
        private Node tail = null;


        public Node addNode(int value) {
            Node newNode = new Node(value);

            if (head == null) {
                head = newNode;
            } else {
                tail.nextNode = newNode;
            }

            tail = newNode;
            tail.nextNode = head;
            return newNode;
        }

        public boolean containsNode(int searchValue) {
            Node currentNode = head;

            if (head == null) {
                return false;
            } else {
                do {
                    if (currentNode.value == searchValue) {
                        return true;
                    }
                    currentNode = currentNode.nextNode;
                } while (currentNode != head);
                return false;
            }
        }

        public void deleteNode(int valueToDelete) {
            Node currentNode = head;
            if (head == null) { // the list is empty
                return;
            }
            do {
                Node nextNode = currentNode.nextNode;
                if (nextNode.value == valueToDelete) {
                    if (tail == head) { // the list has only one single element
                        head = null;
                        tail = null;
                    } else {
                        currentNode.nextNode = nextNode.nextNode;
                        if (head == nextNode) { //we're deleting the head
                            head = head.nextNode;
                        }
                        if (tail == nextNode) { //we're deleting the tail
                            tail = currentNode;
                        }
                    }
                    break;
                }
                currentNode = nextNode;
            } while (currentNode != head);
        }
    }
    class Node {
        int value;
        Node nextNode;

        public Node(int value) {
            this.value = value;
        }
    }
}

