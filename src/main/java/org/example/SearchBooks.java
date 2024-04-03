package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class SearchBooks {
    public static void searchBooks() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Search Criteria (ISBN, Title, Author, Genre, or Publication Year): ");
        String searchCriteria = scanner.nextLine();

        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT b.isbn, b.title, b.author, b.genre, b.publication_year, bc.copy_id, bc.availability " +
                "FROM Books b " +
                "LEFT JOIN BookCopies bc ON b.isbn = bc.isbn " +
                "WHERE b.isbn LIKE ? OR b.title LIKE ? OR b.author LIKE ? OR b.genre LIKE ? OR b.publication_year = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchCriteria + "%");
            stmt.setString(2, "%" + searchCriteria + "%");
            stmt.setString(3, "%" + searchCriteria + "%");
            stmt.setString(4, "%" + searchCriteria + "%");
            stmt.setString(5, searchCriteria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int publicationYear = rs.getInt("publication_year");
                int copyId = rs.getInt("copy_id");
                boolean availability = rs.getBoolean("availability");

                System.out.println("ISBN: " + isbn);
                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Genre: " + genre);
                System.out.println("Publication Year: " + publicationYear);
                System.out.println("Copy ID: " + copyId);
                System.out.println("Availability: " +(availability ? "Available" : "Not Available"));
                            System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
