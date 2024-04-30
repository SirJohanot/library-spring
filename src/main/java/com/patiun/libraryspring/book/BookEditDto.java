package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Objects;

public class BookEditDto {

    @Pattern(regexp = Regexp.WORD, message = "Book title must start with an alphabetical character or a number")
    private String title;

    @NotEmpty(message = "Authors array must not be empty")
    private List<@Pattern(regexp = Regexp.HUMAN_NAME, message = "Author name must be start with an alphabetical character") String> authors;

    @Pattern(regexp = Regexp.WORD, message = "Genre name must start with an alphabetical character or a number")
    private String genre;

    @Pattern(regexp = Regexp.WORD, message = "Publisher name must start with an alphabetical character or a number")
    private String publisher;

    @Min(value = 1900, message = "Publishment year must be at least 1900")
    @Max(value = 2500, message = "Publishment year must be at most 2500")
    private int publishmentYear;

    @Pattern(regexp = Regexp.WORD, message = "Publishment location must start with an alphabetical character or a number")
    private String publishmentLocation;

    @Pattern(regexp = Regexp.ISBN, message = "Book ISBN must be a 10- or 13-digit number")
    private String isbn;

    @Min(value = 0, message = "Amount must be at least 0")
    private int amount;

    public BookEditDto() {
    }

    public BookEditDto(String title, List<String> authors, String genre, String publisher, int publishmentYear, String publishmentLocation, String isbn, int amount) {
        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.publisher = publisher;
        this.publishmentYear = publishmentYear;
        this.publishmentLocation = publishmentLocation;
        this.isbn = isbn;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublishmentYear() {
        return publishmentYear;
    }

    public void setPublishmentYear(int publishmentYear) {
        this.publishmentYear = publishmentYear;
    }

    public String getPublishmentLocation() {
        return publishmentLocation;
    }

    public void setPublishmentLocation(String publishmentLocation) {
        this.publishmentLocation = publishmentLocation;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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
        return publishmentYear == that.publishmentYear && amount == that.amount && Objects.equals(title, that.title) && Objects.equals(authors, that.authors) && Objects.equals(genre, that.genre) && Objects.equals(publisher, that.publisher) && Objects.equals(publishmentLocation, that.publishmentLocation) && Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(authors);
        result = 31 * result + Objects.hashCode(genre);
        result = 31 * result + Objects.hashCode(publisher);
        result = 31 * result + publishmentYear;
        result = 31 * result + Objects.hashCode(publishmentLocation);
        result = 31 * result + Objects.hashCode(isbn);
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return "BookEditDto{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", genre='" + genre + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishmentYear=" + publishmentYear +
                ", publishmentLocation='" + publishmentLocation + '\'' +
                ", isbn='" + isbn + '\'' +
                ", amount=" + amount +
                '}';
    }
}
