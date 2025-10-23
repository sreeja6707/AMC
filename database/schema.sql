-- Create Database
CREATE DATABASE IF NOT EXISTS amc_tracking_system;
USE amc_tracking_system;

-- Users Table
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    directorate VARCHAR(100) NOT NULL,
    full_name VARCHAR(200),
    email VARCHAR(100),
    status ENUM('Active', 'Inactive') DEFAULT 'Active',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Directorate Master Table
CREATE TABLE Directorate_Master (
    directorate_id INT PRIMARY KEY AUTO_INCREMENT,
    directorate_name VARCHAR(100) NOT NULL,
    directorate_code VARCHAR(10) UNIQUE NOT NULL,
    status ENUM('Active', 'Inactive') DEFAULT 'Active'
);

-- Address List Table
CREATE TABLE Address_List (
    address_id INT PRIMARY KEY AUTO_INCREMENT,
    directorate_id INT,
    user_name VARCHAR(200),
    designation VARCHAR(100),
    department VARCHAR(100),
    address TEXT,
    status ENUM('Active', 'Inactive') DEFAULT 'Active',
    FOREIGN KEY (directorate_id) REFERENCES Directorate_Master(directorate_id)
);

-- Parent Table: AMC_Warranty_Details
CREATE TABLE AMC_Warranty_Details (
    control_no VARCHAR(12) PRIMARY KEY,
    amc_user_id VARCHAR(100),
    directorate VARCHAR(100),
    amc_letter_no VARCHAR(100),
    amc_letter_date DATE,
    subject VARCHAR(200),
    gem_order_no VARCHAR(100),
    gem_order_date DATE,
    amc_effect_from DATE,
    valid_upto DATE,
    payment_mode ENUM('Monthly', 'Quarterly', 'Half yearly', 'Annually'),
    amc_order_value DECIMAL(12,2),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    status VARCHAR(50) DEFAULT 'Active'
);

-- Child Table: AMC_Warranty_Item_Details
CREATE TABLE AMC_Warranty_Item_Details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    control_no VARCHAR(12),
    sno INT,
    item_name VARCHAR(255),
    make VARCHAR(255),
    model VARCHAR(255),
    quantity INT,
    FOREIGN KEY (control_no) REFERENCES AMC_Warranty_Details(control_no),
    UNIQUE KEY unique_control_sno (control_no, sno)
);

-- Control Number Sequence Table
CREATE TABLE Control_Number_Sequence (
    id INT PRIMARY KEY AUTO_INCREMENT,
    year INT NOT NULL,
    month INT NOT NULL,
    last_sequence INT DEFAULT 0,
    UNIQUE KEY unique_year_month (year, month)
);

-- Insert Sample Data
INSERT INTO Directorate_Master (directorate_name, directorate_code) VALUES 
('Information Technology', 'IT'),
('Finance', 'FIN'),
('Human Resources', 'HR'),
('Operations', 'OPS');

INSERT INTO Address_List (directorate_id, user_name, designation, department, address) VALUES
(1, 'John Doe', 'System Administrator', 'IT Department', '123 Tech Street'),
(1, 'Jane Smith', 'Developer', 'IT Department', '124 Tech Street'),
(2, 'Bob Johnson', 'Finance Manager', 'Finance Department', '125 Money Street'),
(3, 'Alice Brown', 'HR Manager', 'HR Department', '126 People Street');

INSERT INTO Users (username, password, directorate, full_name, email) VALUES
('admin', 'admin123', 'Information Technology', 'System Administrator', 'admin@company.com'),
('user1', 'user123', 'Finance', 'Finance User', 'finance@company.com');

-- Create indexes for better performance
CREATE INDEX idx_control_no ON AMC_Warranty_Details(control_no);
CREATE INDEX idx_created_date ON AMC_Warranty_Details(created_date);
CREATE INDEX idx_directorate ON Users(directorate);
