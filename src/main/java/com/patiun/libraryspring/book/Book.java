package com.patiun.libraryspring.book;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title", length = 128, nullable = false)
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")}
    )
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_editor",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "editor_id")}
    )
    private List<Editor> editors;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "printing_house_id")
    private PrintingHouse printingHouse;

    @Column(name = "publication_year", nullable = false)
    private int publicationYear;

    @Column(name = "publication_location", length = 64, nullable = false)
    private String publicationLocation;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "pages_number", nullable = false)
    private int pagesNumber;

    @Column(name = "isbn", length = 13)
    private String isbn;

    @Column(name = "udc", length = 64)
    private String udc;

    @Column(name = "bbc", length = 64)
    private String bbc;

    @Column(name = "author_index", length = 8)
    private String authorIndex;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public Book() {
    }
    
    public Book(Integer id, String title, List<Author> authors, List<Editor> editors, Genre genre, Publisher publisher, PrintingHouse printingHouse, int publicationYear, String publicationLocation, String description, int pagesNumber, String isbn, String udc, String bbc, String authorIndex, int amount, boolean isDeleted) {
        this.id = id;
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
        this.authorIndex = authorIndex;
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

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
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

    public PrintingHouse getPrintingHouse() {
        return printingHouse;
    }

    public void setPrintingHouse(PrintingHouse printingHouse) {
        this.printingHouse = printingHouse;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublicationLocation() {
        return publicationLocation;
    }

    public void setPublicationLocation(String publicationLocation) {
        this.publicationLocation = publicationLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUdc() {
        return udc;
    }

    public void setUdc(String udc) {
        this.udc = udc;
    }

    public String getBbc() {
        return bbc;
    }

    public void setBbc(String bbc) {
        this.bbc = bbc;
    }

    public String getAuthorIndex() {
        return authorIndex;
    }

    public void setAuthorIndex(String authorIndex) {
        this.authorIndex = authorIndex;
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
        return publicationYear == book.publicationYear && pagesNumber == book.pagesNumber && amount == book.amount && isDeleted == book.isDeleted && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && Objects.equals(editors, book.editors) && Objects.equals(genre, book.genre) && Objects.equals(publisher, book.publisher) && Objects.equals(printingHouse, book.printingHouse) && Objects.equals(publicationLocation, book.publicationLocation) && Objects.equals(description, book.description) && Objects.equals(isbn, book.isbn) && Objects.equals(udc, book.udc) && Objects.equals(bbc, book.bbc) && Objects.equals(authorIndex, book.authorIndex);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
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
        result = 31 * result + Objects.hashCode(authorIndex);
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
                ", editors=" + editors +
                ", genre=" + genre +
                ", publisher=" + publisher +
                ", printingHouse=" + printingHouse +
                ", publicationYear=" + publicationYear +
                ", publicationLocation='" + publicationLocation + '\'' +
                ", description='" + description + '\'' +
                ", pagesNumber=" + pagesNumber +
                ", isbn='" + isbn + '\'' +
                ", udc='" + udc + '\'' +
                ", bbc='" + bbc + '\'' +
                ", authorIndex='" + authorIndex + '\'' +
                ", amount=" + amount +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
