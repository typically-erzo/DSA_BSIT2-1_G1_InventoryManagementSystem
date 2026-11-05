package inventory.gui;

import inventory.model.Supplier;
import inventory.service.SupplierRegistryService;
import inventory.ds.SupplierHashTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class SupplierPanel extends JPanel implements ActionListener {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, contactField, addressField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private SupplierRegistryService supplierService;

    public SupplierPanel() {
        supplierService = new SupplierRegistryService();
        setLayout(null);
        setBounds(0, 0, 960, 680);

        tableModel = new DefaultTableModel(new String[]{"Supplier ID", "Name", "Contact Info", "Address"}, 0) {
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
                    contactField.setText(tableModel.getValueAt(row, 2).toString());
                    addressField.setText(tableModel.getValueAt(row, 3).toString());
                    idField.setEnabled(false);
                }
            }
        });

        JLabel lblId = new JLabel("Supplier ID:");
        lblId.setBounds(30, 350, 100, 25);
        add(lblId);

        idField = new JTextField();
        idField.setBounds(140, 350, 300, 25);
        add(idField);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(490, 350, 100, 25);
        add(lblName);

        nameField = new JTextField();
        nameField.setBounds(600, 350, 300, 25);
        add(nameField);

        JLabel lblContact = new JLabel("Contact:");
        lblContact.setBounds(30, 390, 100, 25);
        add(lblContact);

        contactField = new JTextField();
        contactField.setBounds(140, 390, 300, 25);
        add(contactField);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(490, 390, 100, 25);
        add(lblAddress);

        addressField = new JTextField();
        addressField.setBounds(600, 390, 300, 25);
        add(addressField);

        addButton = new JButton("Add Supplier");
        addButton.setBounds(30, 450, 160, 35);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update Selected");
        updateButton.setBounds(210, 450, 160, 35);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete Supplier");
        deleteButton.setBounds(390, 450, 160, 35);
        deleteButton.addActionListener(this);
        add(deleteButton);

        clearButton = new JButton("Clear Form");
        clearButton.setBounds(570, 450, 140, 35);
        clearButton.addActionListener(this);
        add(clearButton);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Supplier> suppliers = supplierService.getAllSuppliers();

            SupplierHashTable hashTable = new SupplierHashTable();
            for (Supplier s : suppliers) {
                hashTable.put(s);
            }

            for (Supplier s : hashTable.getAllSuppliers()) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getContactInfo(), s.getAddress()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            if (!idField.isEnabled()) {
                JOptionPane.showMessageDialog(this,
                        "You have selected an existing supplier row.\nClick 'Clear Form' to enter a new Supplier ID, or click 'Update Selected' to update this supplier.",
                        "Action Not Allowed",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || contact.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All fields (Supplier ID, Name, Contact, Address) are required!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Supplier s = new Supplier(id, name, contact, address);
                supplierService.addSupplier(s);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Supplier added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateButton) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || contact.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Select a supplier from the table to update, and make sure all fields are filled.", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Supplier s = new Supplier(id, name, contact, address);
                supplierService.updateSupplier(s);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Supplier updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a supplier to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                supplierService.deleteSupplier(id);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Supplier deleted successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    private void clearForm() {
        idField.setText("");
        idField.setEnabled(true);
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        table.clearSelection();
    }
}


