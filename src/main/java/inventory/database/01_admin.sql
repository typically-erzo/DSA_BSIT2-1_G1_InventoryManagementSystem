CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS stock_orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- Suppliers table
CREATE TABLE suppliers (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- Products table
CREATE TABLE products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    supplier_id VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL
);

-- Stock Orders table
CREATE TABLE stock_orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(50),
    quantity INT NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    order_date DATE NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Users / Admin table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Pre-seed strictly the default admin
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');


