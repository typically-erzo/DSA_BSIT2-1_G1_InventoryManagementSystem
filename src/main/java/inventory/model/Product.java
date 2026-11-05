package inventory.model;

public class Product {
    private String id;
    private String name;
    private String category;
    private int quantity;
    private String supplierId;
    private double price;

    public Product(String id, String name, String category, int quantity, String supplierId, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}