package com.sasan.app;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final LibraryService service;

    public BookController(LibraryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }
}
