package inventory.gui;

import inventory.database.DBConnection;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Inventory Management System - Login");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null); // Pure absolute layout

        JLabel titleLabel = new JLabel("System Login");
        titleLabel.setBounds(185, 30, 200, 30);
        add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(60, 90, 100, 25);
        add(userLabel);

        userField = new JTextField("admin");
        userField.setBounds(160, 90, 200, 30);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(60, 140, 100, 25);
        add(passLabel);

        passField = new JPasswordField("admin123");
        passField.setBounds(160, 140, 200, 30);
        add(passField);

        loginButton = new JButton("Login");
        loginButton.setBounds(160, 200, 200, 35);
        loginButton.addActionListener(this);
        add(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new MainFrame().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Default: admin / admin123", "Access Denied", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

