package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "book_order")
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "rental_type", length = 64)
    @Enumerated(EnumType.STRING)
    private RentalType rentalType;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @NotNull
    @Column(name = "state", length = 64)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    protected BookOrder() {
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

        if (!Objects.equals(id, bookOrder.id)) {
            return false;
        }
        if (!Objects.equals(book, bookOrder.book)) {
            return false;
        }
        if (!Objects.equals(user, bookOrder.user)) {
            return false;
        }
        if (rentalType != bookOrder.rentalType) {
            return false;
        }
        if (!Objects.equals(startDate, bookOrder.startDate)) {
            return false;
        }
        if (!Objects.equals(endDate, bookOrder.endDate)) {
            return false;
        }
        if (!Objects.equals(returnDate, bookOrder.returnDate)) {
            return false;
        }
        return state == bookOrder.state;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (rentalType != null ? rentalType.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
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
