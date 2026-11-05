package inventory.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private ProductPanel productPanel;
    private SupplierPanel supplierPanel;
    private OrderReportPanel orderReportPanel;

    public MainFrame() {
        setTitle("Inventory Management System - Dashboard");
        setSize(980, 740);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null); // Absolute layout

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10, 10, 950, 680);

        productPanel = new ProductPanel();
        supplierPanel = new SupplierPanel();
        orderReportPanel = new OrderReportPanel();

        tabbedPane.addTab("Products & Inventory", productPanel);
        tabbedPane.addTab("Suppliers Directory", supplierPanel);
        tabbedPane.addTab("Stock Orders & Reports", orderReportPanel);

        add(tabbedPane);
    }

    public void refreshDependentPanel() {
        productPanel.refreshTable();
        supplierPanel.refreshTable();
        orderReportPanel.refreshTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handled within modular panels
    }
}
