package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "book_order")
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rental_type", length = 64)
    @Enumerated(EnumType.STRING)
    private RentalType rentalType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "state", length = 64)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    public BookOrder() {
    }

    public BookOrder(Integer id, Book book, User user, RentalType rentalType, LocalDate startDate, LocalDate endDate, LocalDate returnDate, OrderState state) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.rentalType = rentalType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = returnDate;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(RentalType rentalType) {
        this.rentalType = rentalType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookOrder bookOrder = (BookOrder) o;
        return Objects.equals(id, bookOrder.id) && Objects.equals(book, bookOrder.book) && Objects.equals(user, bookOrder.user) && rentalType == bookOrder.rentalType && Objects.equals(startDate, bookOrder.startDate) && Objects.equals(endDate, bookOrder.endDate) && Objects.equals(returnDate, bookOrder.returnDate) && state == bookOrder.state;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(book);
        result = 31 * result + Objects.hashCode(user);
        result = 31 * result + Objects.hashCode(rentalType);
        result = 31 * result + Objects.hashCode(startDate);
        result = 31 * result + Objects.hashCode(endDate);
        result = 31 * result + Objects.hashCode(returnDate);
        result = 31 * result + Objects.hashCode(state);
        return result;
    }

    @Override
    public String toString() {
        return "BookOrder{" +
                "id=" + id +
                ", book=" + book +
                ", user=" + user +
                ", rentalType=" + rentalType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", returnDate=" + returnDate +
                ", state=" + state +
                '}';
    }
}
