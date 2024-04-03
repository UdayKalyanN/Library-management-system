package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class BorrowBook {
    public static void borrowBook() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Borrower ID: ");
        int borrowerId = scanner.nextInt();
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.next();

        Connection conn = DatabaseConnection.getConnection();
        String copySQL = "SELECT copy_id FROM BookCopies WHERE isbn = ? AND availability = true LIMIT 1";

        try (PreparedStatement copyStmt = conn.prepareStatement(copySQL)) {
            copyStmt.setString(1, isbn);
            ResultSet rs = copyStmt.executeQuery();

            if (rs.next()) {
                int copyId = rs.getInt("copy_id");

                String transactionSQL = "INSERT INTO BorrowingTransactions (borrower_id, copy_id, borrow_date) VALUES (?, ?, ?)";
                try (PreparedStatement transactionStmt = conn.prepareStatement(transactionSQL)) {
                    transactionStmt.setInt(1, borrowerId);
                    transactionStmt.setInt(2, copyId);
                    transactionStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    transactionStmt.executeUpdate();

                    String updateCopySQL = "UPDATE BookCopies SET availability = false WHERE copy_id = ?";
                    try (PreparedStatement updateCopyStmt = conn.prepareStatement(updateCopySQL)) {
                        updateCopyStmt.setInt(1, copyId);
                        updateCopyStmt.executeUpdate();
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
