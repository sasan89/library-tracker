package com.sasan.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibraryService {

    private final BookRepository repository;
    private List<Book> books;

    public LibraryService(BookRepository repository) {
        this.repository = repository;
        this.books = new ArrayList<>(repository.loadAll());
    }

    // --- Add / Delete ---

    public void addBook(Book book) {
        books.add(book);
        repository.saveAll(books);
    }

    public boolean deleteBook(String title) {
        boolean removed = books.removeIf(b -> b.title().equalsIgnoreCase(title));
        if (removed) repository.saveAll(books);
        return removed;
    }

    // --- Mark Read / Unread ---

    public boolean markAsRead(String title) {
        return updateReadStatus(title, true);

    }
    public boolean markAsUnread(String title) {
        return updateReadStatus(title, false);
    }

    public boolean updateReadStatus(String title, boolean isRead) {
        Optional<Book> found = findByTitle(title);
        if (found.isEmpty()) return false;

        books = books.stream()
                    .map(b -> b.title().equalsIgnoreCase(title)
                        ? (isRead ? b.markAsRead() : b.markAsUnread())
                        : b)
                    .collect(Collectors.toCollection(ArrayList::new));
        
        repository.saveAll(books);
        return true;
    }

    // --- Search / Filter ---

    public Optional<Book> findByTitle(String title) {
        return books.stream()
                    .filter(b -> b.title().equalsIgnoreCase(title))
                    .findFirst();
    }

    public List<Book> findByAuthor(String author) {
        return books.stream()
                    .filter(b -> b.author().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
    }

    public List<Book> findByGenre(String genre) {
        return books.stream()
                    .filter(b -> b.genre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
    }

    public List<Book> getUnreadBooks() {
        return books.stream()
                    .filter(b -> !b.isRead())
                    .collect(Collectors.toList());
    }

    // --- List All ---

    public List<Book> getAllBooks() {
        return List.copyOf(books);
    }


}
