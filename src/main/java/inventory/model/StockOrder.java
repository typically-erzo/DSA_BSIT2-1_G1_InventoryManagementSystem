package inventory.model;

public class StockOrder {
    private int orderId;
    private String productId;
    private int quantity;
    private double totalCost;
    private String orderDate;

    public StockOrder(int orderId, String productId, int quantity, double totalCost, String orderDate) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.orderDate = orderDate;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
}