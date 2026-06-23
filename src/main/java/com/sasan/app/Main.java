package com.sasan.app;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);
    public static final LibraryService service = 
            new LibraryService(new BookRepository());

    public static void main(String[] args) {

        // Main menu of the library tracker
        System.out.println("📚 Library Book Tracker");
        System.out.println("========================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            running = switch (choice) {
                case "1" -> {
                    addBook();
                    yield true;
                }
                case "2" -> {
                    listBooks();
                    yield true;
                }
                case "3" -> {
                    searchBook();
                    yield true;
                }
                case "4" -> {
                    markRead();
                    yield true;
                }
                case "5" -> {
                    deleteBook();
                    yield true;
                }
                case "6" -> {
                    System.out.println("Goodbye!");
                    yield false;
                }
                default -> {
                    System.out.println("Invalid option, try again.");
                    yield true;
                }
            };
        }

    }

    private static void printMenu() {
        System.out.println("""

                1. Add a book
                2. List all books
                3. Search books
                4. Mark as read / unread
                5. Delete a book
                6. Exit
                Choose:""");
        System.out.println("> ");
    }

    // --- Actions ---

    private static void addBook() {
        System.out.println("Title: ");
        String title = scanner.nextLine();
        System.out.println("Author: ");
        String author = scanner.nextLine();
        System.out.println("Genre: ");
        String genre = scanner.nextLine();
        System.out.println("Year: ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid year – book not added.");
            return;
        }
        service.addBook(new Book(title, author, genre, year, false));
        System.out.println("✔️ Book added.");
    }

    private static void listBooks() {
        List<Book> books = service.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books yet.");
            return;
        }

        System.out.println("\n" + "–".repeat(90));
        System.out.printf("%-35s %-20s %-15s %-6s %s%n",
                "Title", "Author", "Genre", "Year", "Status");
        System.out.println("–".repeat(90));
        books.forEach(b -> System.out.println(b.summary()));
        System.out.println("–".repeat(90));
        System.out.println("Total: " + books.size() + " books");
    }

    private static void searchBook() {
        System.out.println("Search by:  1) Title    2)Author    3) Genre    4) Unread only");
        System.out.println("> ");

        String choice = scanner.nextLine().trim();

        List<Book> results = switch (choice) {
            case "1" -> {
                System.out.println("Title:");
                String title = scanner.nextLine();
                Optional<Book> result = service.findByTitle(title);
                yield result.map(List::of).orElse(List.of());
            }
            case "2" -> {
                System.out.println("Author: ");
                yield service.findByAuthor(scanner.nextLine());
            }
            case "3" -> {
                System.out.println("Genre: ");
                yield service.findByGenre(scanner.nextLine());
            }
            case "4" -> service.getUnreadBooks();
            default -> {
                System.out.println("Invalid.");
                yield List.of();
            }
        };

        if (results.isEmpty()) System.out.println("\nNo books found.");
        else {
            System.out.printf("%n%-35s %-20s %-15s %-6s %s%n",
                "Title", "Author", "Genre", "Year", "Status");
            System.out.println("–".repeat(90));
            results.forEach(b -> System.out.println(b.summary()));
            System.out.println("–".repeat(90));
            System.out.println("Total: " + results.size() + " books");
        }
    }

    private static void markRead() {
        System.out.println("Title: "); String title = scanner.nextLine();
        System.out.println("Mark as (r)ead or (u)nread? ");
        String action = scanner.nextLine().trim();

        boolean success = switch (action.toLowerCase()) {
            case "r" -> service.markAsRead(title);
            case "u" -> service.markAsUnread(title);
            default -> { System.out.println("Invalid option."); yield false; }
        };

        if (success) System.out.println("✔️ Updated.");
        else System.out.println("Book not found.");
    }

    private static void deleteBook() {
        System.out.println("Title to be deleted: ");
        String title = scanner.nextLine();
        
        System.out.println("Are you sure? (y/n): ");
        String confirm = scanner.nextLine();
        
        if(confirm.equalsIgnoreCase("y")) {
            boolean deleted = service.deleteBook(title);
            System.out.println(deleted ? "✔️ Deleted." : "Book not found.");
        } else {
            System.out.println("Cancelled.");
        }
    }

}
