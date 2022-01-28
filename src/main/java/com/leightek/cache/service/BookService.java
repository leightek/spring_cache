package com.leightek.cache.service;

import com.leightek.cache.model.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookService {

    @Cacheable(value = "books", keyGenerator = "customKeyGenerator")
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<Book>();

        books.add(new Book(1, "Getting Started with Kubernetes", "Jonathan Baier"));
        books.add(new Book(2, "Kafka in Action", "Dylan Scott"));

        return books;
    }
}
