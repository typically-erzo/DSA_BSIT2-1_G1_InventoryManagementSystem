package inventory.service;

import inventory.database.DBConnection;
import inventory.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierRegistryService {

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("contact_info"),
                        rs.getString("address")
                ));
            }
        }
        return suppliers;
    }

    public void addSupplier(Supplier s) throws SQLException {
        String query = "INSERT INTO suppliers (id, name, contact_info, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, s.getId());
            pstmt.setString(2, s.getName());
            pstmt.setString(3, s.getContactInfo());
            pstmt.setString(4, s.getAddress());
            pstmt.executeUpdate();
        }
    }

    public void updateSupplier(Supplier s) throws SQLException {
        String query = "UPDATE suppliers SET name=?, contact_info=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getContactInfo());
            pstmt.setString(3, s.getAddress());
            pstmt.setString(4, s.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteSupplier(String id) throws SQLException {
        String query = "DELETE FROM suppliers WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }
}