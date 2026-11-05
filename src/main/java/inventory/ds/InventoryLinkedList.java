package inventory.ds;

import inventory.model.Product;
import java.util.ArrayList;
import java.util.List;

public class InventoryLinkedList {
    public static class Node {
        public Product product;
        public Node next;

        public Node(Product product) {
            this.product = product;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public InventoryLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(Product product) {
        Node newNode = new Node(product);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public boolean remove(String productId) {
        if (head == null) return false;
        if (head.product.getId().equals(productId)) {
            head = head.next;
            size--;
            return true;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.product.getId().equals(productId)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public List<Product> toList() {
        List<Product> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.product);
            current = current.next;
        }
        return list;
    }

    public int size() {
        return size;
    }
}

