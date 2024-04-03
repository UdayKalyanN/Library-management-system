package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
public class ReturnBook {
    public static void returnBook() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Transaction ID: ");
        int transactionId = scanner.nextInt();

        Connection conn = DatabaseConnection.getConnection();
        String transactionSQL = "UPDATE BorrowingTransactions SET return_date = ? WHERE transaction_id = ?";

        try (PreparedStatement transactionStmt = conn.prepareStatement(transactionSQL)) {
            transactionStmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            transactionStmt.setInt(2, transactionId);
            transactionStmt.executeUpdate();

            String updateCopySQL = "UPDATE BookCopies SET availability = true WHERE copy_id = (SELECT copy_id FROM BorrowingTransactions WHERE transaction_id = ?)";
            try (PreparedStatement updateCopyStmt = conn.prepareStatement(updateCopySQL)) {
                updateCopyStmt.setInt(1, transactionId);
                updateCopyStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
