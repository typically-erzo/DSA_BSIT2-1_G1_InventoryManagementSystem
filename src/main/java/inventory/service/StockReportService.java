package inventory.service;

import inventory.database.DBConnection;
import inventory.model.StockOrder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockReportService {

    public List<StockOrder> getAllOrders() throws SQLException {
        List<StockOrder> orders = new ArrayList<>();
        String query = "SELECT * FROM stock_orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                orders.add(new StockOrder(
                        rs.getInt("order_id"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_cost"),
                        rs.getString("order_date")
                ));
            }
        }
        return orders;
    }

    public void placeOrder(StockOrder order) throws SQLException {
        String query = "INSERT INTO stock_orders (product_id, quantity, total_cost, order_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, order.getProductId());
            pstmt.setInt(2, order.getQuantity());
            pstmt.setDouble(3, order.getTotalCost());
            pstmt.setString(4, order.getOrderDate());
            pstmt.executeUpdate();
        }
        
        // Update product quantity in stock
        String updateProd = "UPDATE products SET quantity = quantity + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateProd)) {
            pstmt.setInt(1, order.getQuantity());
            pstmt.setString(2, order.getProductId());
            pstmt.executeUpdate();
        }
    }
}