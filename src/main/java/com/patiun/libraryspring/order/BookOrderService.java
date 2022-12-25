package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BookOrderService {

    private final BookOrderRepository orderRepository;

    @Autowired
    public BookOrderService(BookOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(Integer bookId, Integer userId, RentalType type, Integer days) {
        User orderingUser = new User();
        orderingUser.setId(userId);

        Book orderedBook = new Book();
        orderedBook.setId(bookId);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        BookOrder newOrder = new BookOrder(null, orderedBook, orderingUser, type, startDate, endDate, null, OrderState.PLACED);

        orderRepository.save(newOrder);
    }
}
