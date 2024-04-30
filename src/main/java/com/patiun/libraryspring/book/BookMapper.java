package com.patiun.libraryspring.book;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public Book toBook(BookEditDto editDto) {
        String title = editDto.getTitle();
        List<Author> authors = editDto.getAuthors()
                .stream()
                .map(Author::new)
                .toList();
        Genre genre = new Genre(null, editDto.getGenre());
        Publisher publisher = new Publisher(null, editDto.getPublisher());
        int publishmentYear = editDto.getPublishmentYear();
        String publishmentLocation = editDto.getPublishmentLocation();
        String isbn = editDto.getIsbn();
        int amount = editDto.getAmount();

        return new Book(null, title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount, false);
    }
}
