package com.sasan.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    private LibraryService service;
    private FakeBookStore store;

    @BeforeEach
    void setUp() {
        store = new FakeBookStore();
        service = new LibraryService(store);
    }

    @Test
    void addBookShouldStoreBook() {
        Book book = new Book("Dune", "Frank Herbert",
                "Sci-Fi", 1965, false);
        service.addBook(book);

        assertEquals(1, service.getAllBooks().size());
        assertTrue(service.findByTitle("Dune").isPresent());

    }

    @Test
    void findByAuthorShouldReturnMatchingBooks() {
        service.addBook(
                new Book("Dune", "Frank Herbert",
                        "Sci-Fi", 1965, false));
        service.addBook(
                new Book("Dune Messiah", "Frank Herbert",
                        "Sci-Fi", 1969, true));
        service.addBook(
                new Book("1984", "George Orwell",
                        "Dustopian", 1949, false));

        List<Book> results = service.findByAuthor("Frank Herbert");

        assertEquals(2, results.size());
    }

    @Test
    void markAsReadShouldUpdateBookStatus() {
        service.addBook(new Book(
                "Dune", "Frank Herbert", "Sci-Fi", 1965, false));
        boolean updated = service.markAsRead("Dune");
        Book updatedBook = service.findByTitle("Dune").orElseThrow();

        assertTrue(updated);
        assertTrue(updatedBook.isRead());
    }

    @Test
    void deleteBookShouldRemoveExistingBook() {
        service.addBook(new Book("Dune", "Frank Herbert",
                "Sci-Fi", 1965, false));
        service.addBook(new Book("Dune Messiah", "Frank Herbert",
                "Sci-Fi", 1969, true));
        service.addBook(new Book("1984", "George Orwell",
                "Dystopian", 1940, false));


        List<Book> unread = service.getUnreadBooks();

        assertEquals(2, unread.size());
        assertTrue(unread.stream().allMatch(book -> !book.isRead()));
    }

    static class FakeBookStore implements BookStore {
        private List<Book> books = new ArrayList<>();

        @Override
        public List<Book> loadAll() {
            return new ArrayList<>(books);
        }

        @Override
        public void saveAll(List<Book> books) {
            this.books = new ArrayList<>();
        }
    }

}
