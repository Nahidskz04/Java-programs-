import java.sql.*;
import java.util.Scanner;

public class DiaryApp {
    private static final String DB_URL = "jdbc:sqlite:diary.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            // Create the diary_entries table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS diary_entries (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, note TEXT);";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nDiary Application");
            System.out.println("1. Add Entry");
            System.out.println("2. View Entries");
            System.out.println("3. Edit Entry");
            System.out.println("4. Delete Entry");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1 -> addEntry(scanner);
                case 2 -> viewEntries();
                case 3 -> editEntry(scanner);
                case 4 -> deleteEntry(scanner);
                case 5 -> {
                    System.out.println("Exiting application...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addEntry(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter your note: ");
        String note = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO diary_entries (date, note) VALUES (?, ?)");) {
            pstmt.setString(1, date);
            pstmt.setString(2, note);
            pstmt.executeUpdate();
            System.out.println("Entry added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding entry: " + e.getMessage());
        }
    }

    private static void viewEntries() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM diary_entries ORDER BY date DESC")) {
            
            System.out.println("\nDiary Entries:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Date: " + rs.getString("date") + ", Note: " + rs.getString("note"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching entries: " + e.getMessage());
        }
    }

    private static void editEntry(Scanner scanner) {
        System.out.print("Enter the ID of the entry to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new note: ");
        String newNote = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE diary_entries SET note = ? WHERE id = ?")) {
            pstmt.setString(1, newNote);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Entry updated successfully.");
            } else {
                System.out.println("Entry not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating entry: " + e.getMessage());
        }
    }

    private static void deleteEntry(Scanner scanner) {
        System.out.print("Enter the ID of the entry to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM diary_entries WHERE id = ?")) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Entry deleted successfully.");
            } else {
                System.out.println("Entry not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting entry: " + e.getMessage());
        }
    }
}
