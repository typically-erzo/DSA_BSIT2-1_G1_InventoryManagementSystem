package inventory.gui;

import inventory.model.StockOrder;
import inventory.service.StockReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrderReportPanel extends JPanel implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField prodIdField, qtyField, costField;
    private JButton orderButton, refreshButton;
    private StockReportService reportService;

    public OrderReportPanel() {
        reportService = new StockReportService();
        setLayout(null);
        setBounds(0, 0, 960, 680);

        //Table cells are read-only
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Product ID", "Quantity", "Total Cost ($)", "Order Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 900, 300);
        add(scrollPane);

        JLabel lblProdId = new JLabel("Product ID:");
        lblProdId.setBounds(30, 360, 100, 25);
        add(lblProdId);

        prodIdField = new JTextField();
        prodIdField.setBounds(140, 360, 280, 25);
        add(prodIdField);

        JLabel lblQty = new JLabel("Quantity:");
        lblQty.setBounds(490, 360, 100, 25);
        add(lblQty);

        qtyField = new JTextField();
        qtyField.setBounds(600, 360, 280, 25);
        add(qtyField);

        JLabel lblCost = new JLabel("Total Cost ($):");
        lblCost.setBounds(30, 400, 100, 25);
        add(lblCost);

        costField = new JTextField();
        costField.setBounds(140, 400, 280, 25);
        add(costField);

        orderButton = new JButton("Place Stock Order");
        orderButton.setBounds(30, 460, 180, 35);
        orderButton.addActionListener(this);
        add(orderButton);

        refreshButton = new JButton("Refresh Report");
        refreshButton.setBounds(230, 460, 180, 35);
        refreshButton.addActionListener(this);
        add(refreshButton);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<StockOrder> orders = reportService.getAllOrders();
            for (StockOrder o : orders) {
                tableModel.addRow(new Object[]{o.getOrderId(), o.getProductId(), o.getQuantity(), o.getTotalCost(), o.getOrderDate()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == orderButton) {
            String prodId = prodIdField.getText().trim();
            String qtyStr = qtyField.getText().trim();
            String costStr = costField.getText().trim();

            //Defensive input validation before parsing numbers
            if (prodId.isEmpty() || qtyStr.isEmpty() || costStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all missing fields (Product ID, Quantity, Total Cost).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyStr);
                double totalCost = Double.parseDouble(costStr);

                StockOrder order = new StockOrder(
                        0,
                        prodId,
                        quantity,
                        totalCost,
                        LocalDate.now().toString()
                );
                reportService.placeOrder(order);
                refreshTable();
                prodIdField.setText("");
                qtyField.setText("");
                costField.setText("");
                JOptionPane.showMessageDialog(this, "Stock order placed & inventory updated successfully.");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Quantity must be an integer and Total Cost must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error placing order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == refreshButton) {
            refreshTable();
        }
    }
}

