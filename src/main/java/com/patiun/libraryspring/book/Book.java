package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Book title must not be blank")
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

    @NotNull(message = "Genre must not be null")
    @Valid
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @NotNull(message = "Publisher must not be null")
    @Valid
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @NotNull(message = "Publishment year must not be null")
    @Min(value = 1900, message = "Publishment year must be at least 1900")
    @Max(value = 2500, message = "Publishment year must be at most 2500")
    @Column(name = "publishment_year")
    private int publishmentYear;

    @NotNull(message = "Amount must not be null")
    @Min(value = 0, message = "Amount must be at least 0")
    @Column(name = "amount")
    private Integer amount;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Book(Integer id, String title, List<Author> authors, @NotNull Genre genre, @NotNull Publisher publisher, int publishmentYear, @NotNull Integer amount, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.publisher = publisher;
        this.publishmentYear = publishmentYear;
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

    public @NotNull Integer getAmount() {
        return amount;
    }

    public void setAmount(@NotNull Integer amount) {
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

        if (isDeleted != book.isDeleted) {
            return false;
        }
        if (!Objects.equals(id, book.id)) {
            return false;
        }
        if (!Objects.equals(title, book.title)) {
            return false;
        }
        if (!Arrays.equals(authors.toArray(), book.authors.toArray())) {
            return false;
        }
        if (!Objects.equals(genre, book.genre)) {
            return false;
        }
        if (!Objects.equals(publisher, book.publisher)) {
            return false;
        }
        if (!Objects.equals(publishmentYear, book.publishmentYear)) {
            return false;
        }
        return Objects.equals(amount, book.amount);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + publishmentYear;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (isDeleted ? 1 : 0);
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
                ", amount=" + amount +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
