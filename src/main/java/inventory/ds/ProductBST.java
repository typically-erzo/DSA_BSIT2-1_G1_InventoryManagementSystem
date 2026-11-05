package inventory.ds;

import inventory.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductBST {
    public static class Node {
        public Product product;
        public Node left, right;

        public Node(Product product) {
            this.product = product;
            left = right = null;
        }
    }

    private Node root;

    public ProductBST() {
        root = null;
    }

    public void insert(Product product) {
        root = insertRec(root, product);
    }

    private Node insertRec(Node root, Product product) {
        if (root == null) {
            return new Node(product);
        }
        if (product.getPrice() < root.product.getPrice()) {
            root.left = insertRec(root.left, product);
        } else {
            root.right = insertRec(root.right, product);
        }
        return root;
    }

    public List<Product> inOrderTraversal() {
        List<Product> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(Node root, List<Product> list) {
        if (root != null) {
            inOrderRec(root.left, list);
            list.add(root.product);
            inOrderRec(root.right, list);
        }
    }
}

