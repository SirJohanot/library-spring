package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.constraints.*;

import java.util.Objects;

public class BookEditDto {

    @NotBlank(message = "Book title must not be blank")
    @Pattern(regexp = Regexp.WORD, message = "Book title must start with an alphabetical character or a number")
    private String title;

    @NotBlank(message = "Authors must not be blank")
    @Pattern(regexp = Regexp.HUMAN_NAMES_DELIMITED_BY_COMMA, message = "Authors line must be human names delimited by commas")
    private String authors;

    @NotBlank(message = "Genre name must not be blank")
    @Pattern(regexp = Regexp.WORD, message = "Genre name must start with an alphabetical character or a number")
    private String genre;

    @NotBlank(message = "Publisher name must not be blank")
    @Pattern(regexp = Regexp.WORD, message = "Publisher name must start with an alphabetical character or a number")
    private String publisher;

    @Min(value = 1900, message = "Publishment year must be at least 1900")
    @Max(value = 2500, message = "Publishment year must be at most 2500")
    private int publishmentYear;

    @NotNull(message = "Amount must not be null")
    @Min(value = 0, message = "Amount must be at least 0")
    private Integer amount;

    public BookEditDto(String title, String authors, String genre, String publisher, int publishmentYear, Integer amount) {
        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.publisher = publisher;
        this.publishmentYear = publishmentYear;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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

        if (!Objects.equals(title, that.title)) {
            return false;
        }
        if (!Objects.equals(authors, that.authors)) {
            return false;
        }
        if (!Objects.equals(genre, that.genre)) {
            return false;
        }
        if (!Objects.equals(publisher, that.publisher)) {
            return false;
        }
        if (!Objects.equals(publishmentYear, that.publishmentYear)) {
            return false;
        }
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + publishmentYear;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BookEditDto{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", genre='" + genre + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishmentYear=" + publishmentYear +
                ", amount=" + amount +
                '}';
    }
}
