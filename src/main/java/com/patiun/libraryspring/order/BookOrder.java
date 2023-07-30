package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "book_order")
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Book must not be null")
    @Valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @NotNull(message = "User must not be null")
    @Valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Rental type must not be null")
    @Column(name = "rental_type", length = 64)
    @Enumerated(EnumType.STRING)
    private RentalType rentalType;

    @NotNull(message = "Start date must not be null")
    @PastOrPresent(message = "Start date must be either in the past or in the present")
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    @Column(name = "end_date")
    private LocalDate endDate;

    @PastOrPresent(message = "Return date must be either in the past or in the present")
    @Column(name = "return_date")
    private LocalDate returnDate;

    @NotNull(message = "State must not be null")
    @Column(name = "state", length = 64)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    public BookOrder(Integer id, @NotNull Book book, @NotNull User user, @NotNull RentalType rentalType, @NotNull LocalDate startDate, @NotNull LocalDate endDate, LocalDate returnDate, @NotNull OrderState state) {
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

    public @NotNull Book getBook() {
        return book;
    }

    public void setBook(@NotNull Book book) {
        this.book = book;
    }

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public @NotNull RentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(@NotNull RentalType rentalType) {
        this.rentalType = rentalType;
    }

    public @NotNull LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotNull LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public @NotNull OrderState getState() {
        return state;
    }

    public void setState(@NotNull OrderState state) {
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
        result = 31 * result + book.hashCode();
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
