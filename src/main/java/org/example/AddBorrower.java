package org.example;

import java.sql.*;
import java.util.Scanner;

class AddBorrower {
    public static int addBorrower() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String contactNumber = scanner.nextLine();

        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO Borrowers (name, email, contact_number) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, contactNumber);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int borrowerId = generatedKeys.getInt(1);
                System.out.println("Borrower added successfully! Borrower ID: " + borrowerId);
                return borrowerId;
            } else {
                System.out.println("Error: Failed to retrieve the generated Borrower ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if there was an error
    }
}
