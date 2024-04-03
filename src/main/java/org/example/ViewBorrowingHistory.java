package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.*;
public class ViewBorrowingHistory {
    public static void viewBorrowingHistory() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Borrower ID: ");
        int borrowerId = scanner.nextInt();

        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT bt.transaction_id, b.title, bt.borrow_date, bt.return_date " +
                "FROM BorrowingTransactions bt " +
                "JOIN BookCopies bc ON bt.copy_id = bc.copy_id " +
                "JOIN Books b ON bc.isbn = b.isbn " +
                "WHERE bt.borrower_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowerId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Borrowing History for Borrower ID: " + borrowerId);
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String title = rs.getString("title");
                Date borrowDate = rs.getDate("borrow_date");
                Date returnDate = rs.getDate("return_date");

                System.out.println("Transaction ID: " + transactionId);
                System.out.println("Book Title: " + title);
                System.out.println("Borrow Date: " + borrowDate);
                System.out.println("Return Date: " + (returnDate != null ? returnDate : "Not Returned Yet"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
