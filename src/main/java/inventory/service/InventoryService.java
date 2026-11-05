package inventory.service;

import inventory.database.DBConnection;
import inventory.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("supplier_id"),
                        rs.getDouble("price")
                ));
            }
        }
        return products;
    }

    public void addProduct(Product p) throws SQLException {
        String query = "INSERT INTO products (id, name, category, quantity, supplier_id, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setString(3, p.getCategory());
            pstmt.setInt(4, p.getQuantity());
            pstmt.setString(5, p.getSupplierId());
            pstmt.setDouble(6, p.getPrice());
            pstmt.executeUpdate();
        }
    }

    public void updateProduct(Product p) throws SQLException {
        String query = "UPDATE products SET name=?, category=?, quantity=?, supplier_id=?, price=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getCategory());
            pstmt.setInt(3, p.getQuantity());
            pstmt.setString(4, p.getSupplierId());
            pstmt.setDouble(5, p.getPrice());
            pstmt.setString(6, p.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteProduct(String id) throws SQLException {
        String query = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }
}