package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddBook {
    public static void addBook() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter Publication Year: ");
        int publicationYear = scanner.nextInt();
        System.out.print("Enter Number of Copies: ");
        int numCopies = scanner.nextInt();

        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO Books (isbn, title, author, genre, publication_year, num_copies) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, genre);
            stmt.setInt(5, publicationYear);
            stmt.setInt(6, numCopies);
            stmt.executeUpdate();

            String copySQL = "INSERT INTO BookCopies (isbn, availability) VALUES (?, ?)";
            try (PreparedStatement copyStmt = conn.prepareStatement(copySQL)) {
                copyStmt.setString(1, isbn);
                copyStmt.setBoolean(2, true);
                for (int i = 0; i < numCopies; i++) {
                    copyStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}