package com.sasan.app;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final Path filePath;

    public BookRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    public List<Book> loadAll() {
        List<Book> books = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return books; // no file yet, return empty
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()){
                    books.add(Book.fromCsv(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }

        return books;
    }

    public void saveAll(List<Book> books) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Book book : books) {
                writer.write(book.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }
}