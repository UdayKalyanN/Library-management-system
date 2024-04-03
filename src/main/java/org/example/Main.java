package org.example;

import java.sql.SQLException;
import java.util.*;

public class Main{
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Add Borrower");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. View Borrowing History");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    AddBook.addBook();
                    break;
                case 2:
                    AddBorrower.addBorrower();
                    break;
                case 3:
                    BorrowBook.borrowBook();
                    break;
                case 4:
                    ReturnBook.returnBook();
                    break;
                case 5:
                    SearchBooks.searchBooks();
                    break;
                case 6:
                    ViewBorrowingHistory.viewBorrowingHistory();
                    break;
                case 7:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}