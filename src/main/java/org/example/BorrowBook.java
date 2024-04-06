package org.example;

import java.sql.*;
import java.util.Scanner;
public class BorrowBook {
    public static void borrowBook() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Borrower ID: ");
        int borrowerId = scanner.nextInt();

        Connection conn = DatabaseConnection.getConnection();

        // Check if the borrower exists
        String checkBorrowerSQL = "SELECT COUNT(*) FROM Borrowers WHERE borrower_id = ?";
        try (PreparedStatement checkBorrowerStmt = conn.prepareStatement(checkBorrowerSQL)) {
            checkBorrowerStmt.setInt(1, borrowerId);
            ResultSet rs = checkBorrowerStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                System.out.println("Borrower ID not found. Please add the borrower first.");
                return;
            }
        }

        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.next();
        String copySQL = "SELECT bc.copy_id, b.title FROM BookCopies bc JOIN Books b ON bc.isbn = b.isbn WHERE bc.isbn = ? AND bc.availability = true LIMIT 1";

        try (PreparedStatement copyStmt = conn.prepareStatement(copySQL)) {
            copyStmt.setString(1, isbn);
            ResultSet rs = copyStmt.executeQuery();

            if (rs.next()) {
                int copyId = rs.getInt("copy_id");
                String title = rs.getString("title");

                String transactionSQL = "INSERT INTO BorrowingTransactions (borrower_id, copy_id, borrow_date) VALUES (?, ?, ?)";
                try (PreparedStatement transactionStmt = conn.prepareStatement(transactionSQL, Statement.RETURN_GENERATED_KEYS)) {
                    transactionStmt.setInt(1, borrowerId);
                    transactionStmt.setInt(2, copyId);
                    transactionStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    transactionStmt.executeUpdate();

                    ResultSet generatedKeys = transactionStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int transactionId = generatedKeys.getInt(1);

                        String updateCopySQL = "UPDATE BookCopies SET availability = false WHERE copy_id = ?";
                        try (PreparedStatement updateCopyStmt = conn.prepareStatement(updateCopySQL)) {
                            updateCopyStmt.setInt(1, copyId);
                            updateCopyStmt.executeUpdate();
                        }

                        System.out.println("Book '" + title + "' borrowed successfully! Transaction ID: " + transactionId);
                    }
                }
            } else {
                System.out.println("No available copies of the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
