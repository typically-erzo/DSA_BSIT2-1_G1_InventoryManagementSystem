package inventory.gui;

import inventory.model.Product;
import inventory.service.InventoryService;
import inventory.ds.ProductBST;
import inventory.ds.InventoryLinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ProductPanel extends JPanel implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, categoryField, qtyField, supplierField, priceField;
    private JButton addButton, updateButton, deleteButton, sortBstButton, clearButton;
    private InventoryService inventoryService;

    public ProductPanel() {
        inventoryService = new InventoryService();
        setLayout(null);
        setBounds(0, 0, 960, 680);

        // Table & ScrollPane
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Quantity", "Supplier ID", "Price ($)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 900, 300);
        add(scrollPane);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    categoryField.setText(tableModel.getValueAt(row, 2).toString());
                    qtyField.setText(tableModel.getValueAt(row, 3).toString());
                    supplierField.setText(tableModel.getValueAt(row, 4).toString());
                    priceField.setText(tableModel.getValueAt(row, 5).toString());
                    idField.setEnabled(false); // Locked for updating existing entity
                }
            }
        });

        // Form Fields (Row 1 - Y: 350)
        JLabel lblId = new JLabel("Product ID:");
        lblId.setBounds(30, 350, 100, 25);
        add(lblId);

        idField = new JTextField();
        idField.setBounds(140, 350, 280, 25);
        add(idField);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(490, 350, 100, 25);
        add(lblName);

        nameField = new JTextField();
        nameField.setBounds(600, 350, 280, 25);
        add(nameField);

        // Form Fields (Row 2 - Y: 390)
        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(30, 390, 100, 25);
        add(lblCategory);

        categoryField = new JTextField();
        categoryField.setBounds(140, 390, 280, 25);
        add(categoryField);

        JLabel lblQty = new JLabel("Quantity:");
        lblQty.setBounds(490, 390, 100, 25);
        add(lblQty);

        qtyField = new JTextField();
        qtyField.setBounds(600, 390, 280, 25);
        add(qtyField);

        // Form Fields (Row 3 - Y: 430)
        JLabel lblSupplier = new JLabel("Supplier ID:");
        lblSupplier.setBounds(30, 430, 100, 25);
        add(lblSupplier);

        supplierField = new JTextField();
        supplierField.setBounds(140, 430, 280, 25);
        add(supplierField);

        JLabel lblPrice = new JLabel("Price ($):");
        lblPrice.setBounds(490, 430, 100, 25);
        add(lblPrice);

        priceField = new JTextField();
        priceField.setBounds(600, 430, 280, 25);
        add(priceField);

        // Action Buttons (Y: 490)
        addButton = new JButton("Add Product");
        addButton.setBounds(30, 490, 150, 35);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update Selected");
        updateButton.setBounds(190, 490, 150, 35);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete Product");
        deleteButton.setBounds(350, 490, 150, 35);
        deleteButton.addActionListener(this);
        add(deleteButton);

        sortBstButton = new JButton("Sort by Price (BST)");
        sortBstButton.setBounds(510, 490, 180, 35);
        sortBstButton.addActionListener(this);
        add(sortBstButton);

        clearButton = new JButton("Clear Form");
        clearButton.setBounds(700, 490, 140, 35);
        clearButton.addActionListener(this);
        add(clearButton);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Product> products = inventoryService.getAllProducts();
            InventoryLinkedList list = new InventoryLinkedList();
            for (Product p : products) {
                list.add(p);
            }

            for (Product p : list.toList()) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getSupplierId(), p.getPrice()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Guard: If an existing row was selected, instruct the user to use Clear Form first or click Update
            if (!idField.isEnabled()) {
                JOptionPane.showMessageDialog(this, 
                    "You have selected an existing product row.\nClick 'Clear Form' to enter a new Product ID, or click 'Update Selected' to update this product.", 
                    "Action Not Allowed", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String supplierId = supplierField.getText().trim();
            String qtyStr = qtyField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || category.isEmpty() || supplierId.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all text fields before adding a product.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyStr);
                double price = Double.parseDouble(priceStr);

                Product p = new Product(id, name, category, quantity, supplierId, price);
                inventoryService.addProduct(p);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product added successfully.");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Quantity must be an integer and Price must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateButton) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String supplierId = supplierField.getText().trim();
            String qtyStr = qtyField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || category.isEmpty() || supplierId.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a product from the table to update, and make sure all fields are filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyStr);
                double price = Double.parseDouble(priceStr);

                Product p = new Product(id, name, category, quantity, supplierId, price);
                inventoryService.updateProduct(p);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product updated successfully.");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Quantity must be an integer and Price must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                inventoryService.deleteProduct(id);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == sortBstButton) {
            try {
                List<Product> products = inventoryService.getAllProducts();
                ProductBST bst = new ProductBST();
                for (Product p : products) {
                    bst.insert(p);
                }
                List<Product> sorted = bst.inOrderTraversal();
                tableModel.setRowCount(0);
                for (Product p : sorted) {
                    tableModel.addRow(new Object[]{
                            p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getSupplierId(), p.getPrice()
                    });
                }
                JOptionPane.showMessageDialog(this, "Products sorted by price using Binary Search Tree (BST).");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    private void clearForm() {
        idField.setText("");
        idField.setEnabled(true);
        nameField.setText("");
        categoryField.setText("");
        qtyField.setText("");
        supplierField.setText("");
        priceField.setText("");
        table.clearSelection();
    }
}


