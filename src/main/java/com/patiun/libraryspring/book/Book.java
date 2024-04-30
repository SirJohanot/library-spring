package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Pattern(regexp = Regexp.WORD, message = "Book title must start with an alphabetical character or a number")
    @Column(name = "title", length = 64)
    private String title;

    @NotEmpty(message = "Author list must not be empty")
    @Valid
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")}
    )
    private List<Author> authors;

    @Valid
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Valid
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Min(value = 1900, message = "Publishment year must be at least 1900")
    @Max(value = 2500, message = "Publishment year must be at most 2500")
    @Column(name = "publishment_year")
    private int publishmentYear;

    @Pattern(regexp = Regexp.WORD, message = "Publishment location must start with an alphabetical character or a number")
    @Column(name = "publishment_location", length = 64)
    private String publishmentLocation;

    @Pattern(regexp = Regexp.ISBN, message = "Book ISBN must be a 10- or 13-digit number")
    @Column(name = "isbn", length = 13)
    private String isbn;

    @Min(value = 0, message = "Amount must be at least 0")
    @Column(name = "amount")
    private int amount;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Book() {
    }

    public Book(Integer id, String title, List<Author> authors, @NotNull Genre genre, @NotNull Publisher publisher, int publishmentYear, String publishmentLocation, String isbn, int amount, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.publisher = publisher;
        this.publishmentYear = publishmentYear;
        this.publishmentLocation = publishmentLocation;
        this.isbn = isbn;
        this.amount = amount;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public @NotNull Genre getGenre() {
        return genre;
    }

    public void setGenre(@NotNull Genre genre) {
        this.genre = genre;
    }

    public @NotNull Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(@NotNull Publisher publisher) {
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Book book = (Book) o;
        return publishmentYear == book.publishmentYear && amount == book.amount && isDeleted == book.isDeleted && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && Objects.equals(genre, book.genre) && Objects.equals(publisher, book.publisher) && Objects.equals(publishmentLocation, book.publishmentLocation) && Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(authors);
        result = 31 * result + Objects.hashCode(genre);
        result = 31 * result + Objects.hashCode(publisher);
        result = 31 * result + publishmentYear;
        result = 31 * result + Objects.hashCode(publishmentLocation);
        result = 31 * result + Objects.hashCode(isbn);
        result = 31 * result + amount;
        result = 31 * result + Boolean.hashCode(isDeleted);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", genre=" + genre +
                ", publisher=" + publisher +
                ", publishmentYear=" + publishmentYear +
                ", publishmentLocation='" + publishmentLocation + '\'' +
                ", isbn='" + isbn + '\'' +
                ", amount=" + amount +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
