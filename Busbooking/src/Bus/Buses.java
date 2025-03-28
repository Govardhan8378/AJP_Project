package Bus;
import java.sql.*;
import java.util.Scanner;

public class Buses{
    private static final String URL = "jdbc:mysql://localhost:3306/bus_ticket_system";
    private static final String USER = "root";
    private static final String PASSWORD = "system";
    private static Connection conn;
    private static Scanner scanner = new Scanner(System.in);
    private static int loggedInUserId = -1;
    private static boolean isAdmin = false;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("\n🚍 Welcome to Bus Ticket Booking System 🚍\n");
            while (true) {
                System.out.println("1. Register\n2. Login\n3. Admin Login\n4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 : registerUser();break;
                    case 2 : loginUser();break;
                    case 3 : adminLogin();break;
                    case 4 : {
                        System.out.println("Exiting... Thank you!");
                        conn.close();
                        return;
                    }
                    default : System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerUser() throws SQLException {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, phone) VALUES (?, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, phone);
        stmt.executeUpdate();
        System.out.println("✅ Registration Successful!");
    }

    private static void loginUser() throws SQLException {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users WHERE username=? AND password=?");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            loggedInUserId = rs.getInt("user_id");
            System.out.println("✅ Login Successful!");
            userDashboard();
        } else {
            System.out.println("❌ Invalid Username or Password!");
        }
    }

    private static void adminLogin() throws SQLException {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            isAdmin = true;
            System.out.println("✅ Admin Login Successful!");
            adminDashboard();
        } else {
            System.out.println("❌ Invalid Admin Credentials!");
        }
    }

    private static void userDashboard() throws SQLException {
        while (true) {
            System.out.println("\n1. View Buses\n2. Book Ticket\n3. Cancel Ticket\n4. View Wallet\n5. Add Money\n6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 :viewBuses(); break;
                case 2 : bookTicket(); break;
                case 3 : cancelTicket();break;
                case 4 : viewWallet();break;
                case 5 : addMoney();break;
                case 6 : {
                    System.out.println("Logging out...");
                    loggedInUserId = -1;
                    return;
                }
                default : System.out.println("Invalid choice! Try again.");
            }
        }
    }
 // Book a ticket for a user
    private static void bookTicket() throws SQLException {
        System.out.print("Enter Bus ID: ");
        int busId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Your Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Your Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Gender (Male/Female/Other): ");
        String gender = scanner.nextLine();

        // Insert passenger details into the database
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO passengers (user_id, name, age, gender, bus_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, loggedInUserId);
        stmt.setString(2, name);
        stmt.setInt(3, age);
        stmt.setString(4, gender);
        stmt.setInt(5, busId);
        stmt.executeUpdate();

        // Retrieve generated passenger ID
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	System.out.println("\n🚌 **Bus Ticket Receipt** 🎟️");
            System.out.println("+--------------------------------------+");
            System.out.printf("| bus ID  : %-20d |\n", busId);
            System.out.printf("| Name          : %-20s |\n", name);
            System.out.printf("| Age           : %-20d |\n", age);
            System.out.printf("| Gender        : %-20s |\n", gender);
            System.out.println("| Passenger ID: " + generatedKeys.getInt(1));
            System.out.println("+--------------------------------------+");
            System.out.println("✅ **Ticket Booked Successfully!** Enjoy your journey. 🚍");
            System.out.println("✅ Ticket Booked! Passenger ID: " + generatedKeys.getInt(1));
        }
    }

    // Cancel a booked ticket
    private static void cancelTicket() throws SQLException {
        System.out.print("Enter Passenger ID to Cancel Ticket: ");
        int passengerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM passengers WHERE passenger_id = ? AND user_id = ?");
        stmt.setInt(1, passengerId);
        stmt.setInt(2, loggedInUserId);
        int rowsDeleted = stmt.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("✅ Ticket Canceled Successfully!");
        } else {
            System.out.println("❌ Ticket not found or doesn't belong to you!");
        }
    }

    // View wallet balance
    private static void viewWallet() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM users WHERE user_id = ?");
        stmt.setInt(1, loggedInUserId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            System.out.println("💰 Your Wallet Balance: ₹" + rs.getDouble("balance"));
        } else {
            System.out.println("❌ Wallet not found!");
        }
    }

    // Add money to wallet
    private static void addMoney() throws SQLException {
        System.out.print("Enter Amount to Add: ₹");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET balance = balance + ? WHERE user_id = ?");
        stmt.setDouble(1, amount);
        stmt.setInt(2, loggedInUserId);
        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("✅ Money Added Successfully!");
        } else {
            System.out.println("❌ Failed to add money!");
        }
    }

   
    private static void adminDashboard() throws SQLException {
        while (true) {
        	 System.out.println("\n🔧 **Admin Dashboard** 🔧");
             System.out.println("1. Add Bus\n2. Delete Bus\n3. View Users\n4. View Bookings\n5. View All Buses\n6. Logout");
             System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addBus();
                case 2 -> deleteBus();
                case 3 -> viewUsers();
                case 4 -> viewBookings();
                case 5 -> viewAllBuses();
                case 6 -> {
                    System.out.println("Logging out...");
                    isAdmin = false;
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
 // View all buses (Admin)
    private static void viewAllBuses() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM buses");

        System.out.println("\n🚌 **All Buses** 🚌");
        System.out.println("+----+------------------+---------------+---------------+---------+---------------+");
        System.out.println("| ID | Bus Name        | Source        | Destination   | Price   | Seats Left    |");
        System.out.println("+----+------------------+---------------+---------------+---------+---------------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-16s | %-13s | %-13s | ₹%-6.2f | %-13d |\n",
                    rs.getInt("bus_id"),
                    rs.getString("bus_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getDouble("price"),
                    rs.getInt("seats_available"));
        }
        System.out.println("+----+------------------+---------------+---------------+---------+---------------+");
    }

    
    private static void addBus() throws SQLException {
        System.out.print("Enter Bus Name: ");
        String busName = scanner.nextLine();
        System.out.print("Enter Source: ");
        String source = scanner.nextLine();
        System.out.print("Enter Destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter Price: ₹");
        double price = scanner.nextDouble();
        System.out.print("Enter Total Seats Available: ");
        int seatsAvailable = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO buses (bus_name, source, destination, price, seats_available) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, busName);
        stmt.setString(2, source);
        stmt.setString(3, destination);
        stmt.setDouble(4, price);
        stmt.setInt(5, seatsAvailable);
        stmt.executeUpdate();
        
        System.out.println("✅ Bus Added Successfully!");
    }


    private static void deleteBus() throws SQLException {
        System.out.print("Enter Bus ID to Delete: ");
        int busId = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM buses WHERE bus_id = ?");
        stmt.setInt(1, busId);
        int rowsDeleted = stmt.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("✅ Bus Deleted Successfully!");
        } else {
            System.out.println("❌ Bus Not Found!");
        }
    }

    private static void viewBuses() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM buses");
        
        System.out.println("\n🚌 Available Buses:\n");
        System.out.println("+----+--------------+-----------+-------------+--------+----------------+");
        System.out.println("| ID |   Bus Name   |  Source   | Destination | Price  | Seats Available |");
        System.out.println("+----+--------------+-----------+-------------+--------+----------------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-12s | %-9s | %-11s | ₹%-5.2f | %-14d |\n",
                rs.getInt("bus_id"), rs.getString("bus_name"), rs.getString("source"),
                rs.getString("destination"), rs.getDouble("price"), rs.getInt("seats_available"));
        }
        System.out.println("+----+--------------+-----------+-------------+--------+----------------+\n");
    }

    private static void viewUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        
        System.out.println("\n👤 Registered Users:\n");
        System.out.println("+----+----------------+-------------+-----------+");
        System.out.println("| ID |   Username     |  Phone      | Balance   |");
        System.out.println("+----+----------------+-------------+-----------+");

        while (rs.next()) {
            System.out.printf("| %-2d | %-14s | %-11s | ₹%-7.2f |\n",
                rs.getInt("user_id"), rs.getString("username"), rs.getString("phone"), rs.getDouble("balance"));
        }
        System.out.println("+----+----------------+-------------+-----------+\n");
    }

    private static void viewBookings() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT p.passenger_id, p.user_id, u.username, p.bus_id, b.bus_name " +
                                         "FROM passengers p " +
                                         "JOIN users u ON p.user_id = u.user_id " +
                                         "JOIN buses b ON p.bus_id = b.bus_id");
        
        System.out.println("\n🎟️ Booked Tickets:\n");
        System.out.println("+----+--------+----------------+--------+--------------+");
        System.out.println("| PID | UID  |   Username      | Bus ID |   Bus Name   |");
        System.out.println("+----+--------+----------------+--------+--------------+");

        while (rs.next()) {
            System.out.printf("| %-2d  | %-4d  | %-14s | %-6d | %-12s |\n",
                rs.getInt("passenger_id"), rs.getInt("user_id"), rs.getString("username"),
                rs.getInt("bus_id"), rs.getString("bus_name"));
        }
        System.out.println("+----+--------+----------------+--------+--------------+\n");
    }

}
