package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

public class BookEditDto {

    @NotNull(message = "Title must not be null")
    @Pattern(regexp = Regexp.WORD, message = "Book title must start with an alphabetical character or a number")
    private String title;

    @NotEmpty(message = "Authors array must not be empty")
    private List<@Pattern(regexp = Regexp.HUMAN_NAME, message = "Author name must start with an alphabetical character") String> authors;

    @NotEmpty(message = "Editors array must not be empty")
    private List<@Valid EditorDto> editors;

    @NotNull(message = "Genre must not be null")
    @Pattern(regexp = Regexp.WORD, message = "Genre name must start with an alphabetical character or a number")
    private String genre;

    @NotNull(message = "Publisher must not be null")
    @Valid
    private PublisherDto publisher;

    @NotNull(message = "Printing house must not be null")
    @Valid
    private PrintingHouseDto printingHouse;

    @Min(value = 1900, message = "Publication year must be at least 1900")
    @Max(value = 2500, message = "Publication year must be at most 2500")
    private int publicationYear;

    @NotNull(message = "Publication location must not be null")
    @Pattern(regexp = Regexp.WORD, message = "Publication location must start with an alphabetical character or a number")
    private String publicationLocation;

    private String description;

    @Min(value = 2, message = "Pages number must be at least 2")
    private int pagesNumber;

    @Pattern(regexp = Regexp.ISBN, message = "Book ISBN must be a 10- or 13-digit number")
    private String isbn;

    @Pattern(regexp = Regexp.UDC_BBC, message = "Invalid UDC format")
    private String udc;

    @Pattern(regexp = Regexp.UDC_BBC, message = "Invalid BBC format")
    private String bbc;

    @Min(value = 0, message = "Amount must be at least 0")
    private int amount;

    public BookEditDto() {
    }

    public BookEditDto(String title, List<@Pattern(regexp = Regexp.HUMAN_NAME, message = "Author name must start with an alphabetical character") String> authors, List<@Valid EditorDto> editors, String genre, PublisherDto publisher, PrintingHouseDto printingHouse, int publicationYear, String publicationLocation, String description, int pagesNumber, String isbn, String udc, String bbc, int amount) {
        this.title = title;
        this.authors = authors;
        this.editors = editors;
        this.genre = genre;
        this.publisher = publisher;
        this.printingHouse = printingHouse;
        this.publicationYear = publicationYear;
        this.publicationLocation = publicationLocation;
        this.description = description;
        this.pagesNumber = pagesNumber;
        this.isbn = isbn;
        this.udc = udc;
        this.bbc = bbc;
        this.amount = amount;
    }

    public @NotNull(message = "Title must not be null") @Pattern(regexp = Regexp.WORD, message = "Book title must start with an alphabetical character or a number") String getTitle() {
        return title;
    }

    public void setTitle(@NotNull(message = "Title must not be null") @Pattern(regexp = Regexp.WORD, message = "Book title must start with an alphabetical character or a number") String title) {
        this.title = title;
    }

    public @NotEmpty(message = "Authors array must not be empty") List<@Pattern(regexp = Regexp.HUMAN_NAME, message = "Author name must start with an alphabetical character") String> getAuthors() {
        return authors;
    }

    public void setAuthors(@NotEmpty(message = "Authors array must not be empty") List<@Pattern(regexp = Regexp.HUMAN_NAME, message = "Author name must start with an alphabetical character") String> authors) {
        this.authors = authors;
    }

    public @NotEmpty(message = "Editors array must not be empty") List<@Valid EditorDto> getEditors() {
        return editors;
    }

    public void setEditors(@NotEmpty(message = "Editors array must not be empty") List<@Valid EditorDto> editors) {
        this.editors = editors;
    }

    public @NotNull(message = "Genre must not be null") @Pattern(regexp = Regexp.WORD, message = "Genre name must start with an alphabetical character or a number") String getGenre() {
        return genre;
    }

    public void setGenre(@NotNull(message = "Genre must not be null") @Pattern(regexp = Regexp.WORD, message = "Genre name must start with an alphabetical character or a number") String genre) {
        this.genre = genre;
    }

    public @NotNull(message = "Publisher must not be null") @Valid PublisherDto getPublisher() {
        return publisher;
    }

    public void setPublisher(@NotNull(message = "Publisher must not be null") @Valid PublisherDto publisher) {
        this.publisher = publisher;
    }

    public @NotNull(message = "Printing house must not be null") @Valid PrintingHouseDto getPrintingHouse() {
        return printingHouse;
    }

    public void setPrintingHouse(@NotNull(message = "Printing house must not be null") @Valid PrintingHouseDto printingHouse) {
        this.printingHouse = printingHouse;
    }

    @Min(value = 1900, message = "Publication year must be at least 1900")
    @Max(value = 2500, message = "Publication year must be at most 2500")
    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(@Min(value = 1900, message = "Publication year must be at least 1900") @Max(value = 2500, message = "Publication year must be at most 2500") int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public @NotNull(message = "Publication location must not be null") @Pattern(regexp = Regexp.WORD, message = "Publication location must start with an alphabetical character or a number") String getPublicationLocation() {
        return publicationLocation;
    }

    public void setPublicationLocation(@NotNull(message = "Publication location must not be null") @Pattern(regexp = Regexp.WORD, message = "Publication location must start with an alphabetical character or a number") String publicationLocation) {
        this.publicationLocation = publicationLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Min(value = 2, message = "Pages number must be at least 2")
    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(@Min(value = 2, message = "Pages number must be at least 2") int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public @Pattern(regexp = Regexp.ISBN, message = "Book ISBN must be a 10- or 13-digit number") String getIsbn() {
        return isbn;
    }

    public void setIsbn(@Pattern(regexp = Regexp.ISBN, message = "Book ISBN must be a 10- or 13-digit number") String isbn) {
        this.isbn = isbn;
    }

    public @Pattern(regexp = Regexp.WORD, message = "Book UDC must start with a number or a letter") String getUdc() {
        return udc;
    }

    public void setUdc(@Pattern(regexp = Regexp.WORD, message = "Book UDC must start with a number or a letter") String udc) {
        this.udc = udc;
    }

    public @Pattern(regexp = Regexp.WORD, message = "Book BBC must start with a number or a letter") String getBbc() {
        return bbc;
    }

    public void setBbc(@Pattern(regexp = Regexp.WORD, message = "Book BBC must start with a number or a letter") String bbc) {
        this.bbc = bbc;
    }

    @Min(value = 0, message = "Amount must be at least 0")
    public int getAmount() {
        return amount;
    }

    public void setAmount(@Min(value = 0, message = "Amount must be at least 0") int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookEditDto that = (BookEditDto) o;
        return publicationYear == that.publicationYear && pagesNumber == that.pagesNumber && amount == that.amount && Objects.equals(title, that.title) && Objects.equals(authors, that.authors) && Objects.equals(editors, that.editors) && Objects.equals(genre, that.genre) && Objects.equals(publisher, that.publisher) && Objects.equals(printingHouse, that.printingHouse) && Objects.equals(publicationLocation, that.publicationLocation) && Objects.equals(description, that.description) && Objects.equals(isbn, that.isbn) && Objects.equals(udc, that.udc) && Objects.equals(bbc, that.bbc);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(authors);
        result = 31 * result + Objects.hashCode(editors);
        result = 31 * result + Objects.hashCode(genre);
        result = 31 * result + Objects.hashCode(publisher);
        result = 31 * result + Objects.hashCode(printingHouse);
        result = 31 * result + publicationYear;
        result = 31 * result + Objects.hashCode(publicationLocation);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + pagesNumber;
        result = 31 * result + Objects.hashCode(isbn);
        result = 31 * result + Objects.hashCode(udc);
        result = 31 * result + Objects.hashCode(bbc);
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return "BookEditDto{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", editors=" + editors +
                ", genre='" + genre + '\'' +
                ", publisher=" + publisher +
                ", printingHouse=" + printingHouse +
                ", publicationYear=" + publicationYear +
                ", publicationLocation='" + publicationLocation + '\'' +
                ", description='" + description + '\'' +
                ", pagesNumber=" + pagesNumber +
                ", isbn='" + isbn + '\'' +
                ", udc='" + udc + '\'' +
                ", bbc='" + bbc + '\'' +
                ", amount=" + amount +
                '}';
    }
}
