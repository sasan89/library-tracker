package com.sasan.app;

import java.util.List;

public interface BookStore {
    List<Book> loadAll();
    void saveAll(List<Book> books);
}
