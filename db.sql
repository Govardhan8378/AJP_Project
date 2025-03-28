CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    phone VARCHAR(15),
    balance DECIMAL(10,2) DEFAULT 0
);

CREATE TABLE buses (
    bus_id INT AUTO_INCREMENT PRIMARY KEY,
    bus_name VARCHAR(100) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE passengers (
    passenger_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    bus_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (bus_id) REFERENCES buses(bus_id)
);

ALTER TABLE buses MODIFY seats_available INT NOT NULL DEFAULT 40;

-- Insert an Admin
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- Insert Users
INSERT INTO users (username, password, phone, balance) 
VALUES 
('john_doe', 'pass123', '9876543210', 500.00),
('jane_doe', 'securepass', '9123456789', 1000.00);

-- Insert Buses
INSERT INTO buses (bus_name, source, destination, price, seats_available) 
VALUES 
('Luxury Express', 'Hyderabad', 'Vizag', 300.00, 40),
('City Travels', 'Mumbai', 'Pune', 250.00, 30);

-- Insert a Transaction (Adding Money)
INSERT INTO transactions (user_id, amount, transaction_type) 
VALUES (1, 500.00, 'Add Money');

-- Insert a Passenger (Ticket Booking)
INSERT INTO passengers (user_id, name, age, gender, bus_id) 
VALUES (1, 'John Doe', 25, 'Male', 1);
