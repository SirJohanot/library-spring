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

        List<Editor> editors = editDto.getEditors()
                .stream()
                .map(editorDto -> {
                    String role = editorDto.getRole();
                    String name = editorDto.getName();
                    return new Editor(null, role, name);
                })
                .toList();

        Genre genre = new Genre(null, editDto.getGenre());

        PublisherDto publisherDto = editDto.getPublisher();
        String publisherName = publisherDto.getName();
        String publisherPostalCode = publisherDto.getPostalCode();
        String publisherAddress = publisherDto.getAddress();
        Publisher publisher = new Publisher(null, publisherName, publisherPostalCode, publisherAddress);

        PrintingHouseDto printingHouseDto = editDto.getPrintingHouse();
        String printingHouseDtoName = printingHouseDto.getName();
        String printingHouseDtoPostalCode = printingHouseDto.getPostalCode();
        String printingHouseDtoAddress = printingHouseDto.getAddress();
        PrintingHouse printingHouse = new PrintingHouse(null, printingHouseDtoName, printingHouseDtoPostalCode, printingHouseDtoAddress);

        int publicationYear = editDto.getPublicationYear();
        String publicationLocation = editDto.getPublicationLocation();
        String description = editDto.getDescription();
        int pagesNumber = editDto.getPagesNumber();
        String isbn = editDto.getIsbn();
        String udc = editDto.getUdc();
        String bbc = editDto.getBbc();
        String authorIndex = editDto.getAuthorIndex();
        int amount = editDto.getAmount();

        return new Book(null, title, authors, editors, genre, publisher, printingHouse, publicationYear, publicationLocation, description, pagesNumber, isbn, udc, bbc, authorIndex, amount, false);
    }
}
