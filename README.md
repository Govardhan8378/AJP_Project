🚌 Bus Ticket Booking System
📌 Overview
The Bus Ticket Booking System is a console-based Java application that allows users to book bus tickets, manage bookings, and handle payments seamlessly. It features user authentication, admin management, and wallet integration for a smooth ticketing experience. The system is built using Java and MySQL, ensuring a robust and scalable solution.

⚙️ Features
🔹 User Features
✔ User Registration & Login – Users can create accounts and log in securely.
✔ View Available Buses – Users can browse all available buses with details like route, price, and seats available.
✔ Book Tickets – Users can select a bus and book a ticket by providing their details.
✔ Cancel Tickets – Users can cancel previously booked tickets.
✔ Wallet System – Users can view and add money to their wallets.
✔ View Ticket Receipt – After booking, users receive a ticket receipt with complete details.

🔹 Admin Features
✔ Admin Login – Secure access to admin functionalities.
✔ Add & Delete Buses – Admins can manage bus records in the system.
✔ View Users – Admins can list all registered users.
✔ View All Buses – Admins can see all available buses.
✔ View Bookings – Admins can track all bookings in the system.

🗄️ Database Schema
The system consists of the following relational tables:

1️⃣ Users Table
Column Name	Data Type	Description
user_id	INT (PK)	Unique ID for each user
username	VARCHAR	User’s login name
password	VARCHAR	Encrypted password
phone	VARCHAR	Contact number
balance	DECIMAL	Wallet balance
role	VARCHAR	Role type (User/Admin)
2️⃣ Admin Table
Column Name	Data Type	Description
admin_id	INT (PK)	Unique ID for admin
name	VARCHAR	Admin’s name
password	VARCHAR	Encrypted password
3️⃣ Buses Table
Column Name	Data Type	Description
bus_id	INT (PK)	Unique ID for each bus
bus_name	VARCHAR	Bus name
source	VARCHAR	Departure location
destination	VARCHAR	Arrival location
price	DECIMAL	Ticket price
seats_available	INT	Number of available seats
4️⃣ Passengers Table
Column Name	Data Type	Description
passenger_id	INT (PK)	Unique ID for each passenger
user_id	INT (FK)	ID of the user booking the ticket
bus_id	INT (FK)	Bus ID associated with booking
name	VARCHAR	Passenger’s name
age	INT	Age of the passenger
gender	VARCHAR	Gender of the passenger
booking_date	TIMESTAMP	Booking timestamp
status	VARCHAR	Booking status (Active/Canceled)
5️⃣ Payments Table
Column Name	Data Type	Description
payment_id	INT (PK)	Unique payment ID
user_id	INT (FK)	User who made the payment
amount	DECIMAL	Amount paid
transaction_type	VARCHAR	Type (Wallet Top-up, Ticket Booking)
transaction_date	TIMESTAMP	Payment timestamp


🛠️ Tech Stack
Programming Language: Java (JDBC)

Database: MySQL

IDE: Eclipse / VS Code

Version Control: Git

🚀 Installation & Setup
1️⃣ Clone the Repository

sh
Copy
Edit
git clone https://github.com/govardhan8378/bus-ticket-booking-system.git
cd bus-ticket-booking-system
2️⃣ Set Up MySQL Database

Create a new MySQL database: bus_ticket_system

Import the provided SQL schema.

3️⃣ Configure Database Credentials
Update database connection details in the Java file:

java

private static final String URL = "jdbc:mysql://localhost:3306/bus_ticket_system";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
4️⃣ Run the Application
Compile and execute the Java program:

sh

javac BusTicketBooking.java
java BusTicketBooking
📝 Future Enhancements
✅ GUI Implementation using Java Swing/JavaFX
✅ Online Payment Integration (Razorpay, Paytm, etc.)
✅ Mobile App Development

🤝 Contributing
Contributions are welcome! Follow these steps to contribute:

Fork the repository.

Create a new feature branch: git checkout -b feature-name

Commit your changes: git commit -m "Added feature X"

Push the branch: git push origin feature-name

Open a pull request.

📜 License
This project is licensed under the MIT License.

👨‍💻 Author
Developed by Govardhan 👨‍💻

Feel free to connect with me on LinkedIn ("https://www.linkedin.com/in/kethireddy-govardhan-8b9665264/?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app")  🚀

This README.md file provides detailed project documentation for your Bus Ticket Booking System 🚍. Let me know if you need any modifications! 🚀







