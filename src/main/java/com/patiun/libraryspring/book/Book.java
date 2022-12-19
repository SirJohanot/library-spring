package com.patiun.libraryspring.book;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @NotEmpty
    @Column(name = "title", length = 64)
    private String title;

    @NotNull
    @NotEmpty
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")}
    )
    private List<Author> authors;

    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @NotNull
    @Min(1900)
    @Column(name = "publishment_year")
    private Integer publishmentYear;

    @NotNull
    @Min(0)
    @Column(name = "amount")
    private Integer amount;

    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;

    protected Book() {
    }

    public Book(Integer id, String title, List<Author> authors, Genre genre, Publisher publisher, Integer publishmentYear, Integer amount, boolean isDeleted) {
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Integer getPublishmentYear() {
        return publishmentYear;
    }

    public void setPublishmentYear(Integer publishmentYear) {
        this.publishmentYear = publishmentYear;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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
        if (!Objects.equals(authors, book.authors)) {
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
        result = 31 * result + (publishmentYear != null ? publishmentYear.hashCode() : 0);
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
