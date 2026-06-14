package com.sasan.app;

public record Book(String title, String author, String genre, int year, boolean isRead) {

    public Book markAsRead() {
        return new Book(title, author, genre, year, true);
    }

    public Book markAsUnread() {
        return new Book(title, author, genre, year, false);
    }

    public String summary() {
        String status = isRead ? "✓ Read" : "O Unread";
        return String.format("%-35s %-20s %-15s %d  [%s]", title, author, genre, year, status);
    }

    // CSV serialization
    public String toCsv() {
        return String.join(",", title, author, genre,
                String.valueOf(year), String.valueOf(isRead));
    }

    // Reconstruct from CSV
    public static Book fromCsv(String line) {
        String[] parts = line.split(",", 5);
        return new Book(parts[0], parts[1], parts[2],
                Integer.parseInt(parts[3]),
                Boolean.parseBoolean(parts[4]));
    }
}
