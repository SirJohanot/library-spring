package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.book.BookRepository;
import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookOrderService {

    private final BookOrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookOrderService(BookOrderRepository orderRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    public void createOrder(Integer bookId, Integer userId, RentalType type, Integer days) throws ServiceException {
        User orderingUser = new User();
        orderingUser.setId(userId);

        Optional<Book> orderedBookOptional = bookRepository.findById(bookId);
        if (orderedBookOptional.isEmpty()) {
            throw new ServiceException("Could not find a book by id = " + bookId);
        }
        Book orderedBook = orderedBookOptional.get();
        if (orderedBook.isDeleted() || orderedBook.getAmount() <= 0) {
            throw new ServiceException("Could not place an order on book by id = " + bookId + ": the book's either deleted or not in stock");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        BookOrder newOrder = new BookOrder(null, orderedBook, orderingUser, type, startDate, endDate, null, OrderState.PLACED);

        orderRepository.save(newOrder);
    }

    public List<BookOrder> getAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .toList();
    }

    public List<BookOrder> getOrdersOfUser(Integer userId) {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> {
                    User orderingUser = order.getUser();
                    return Objects.equals(orderingUser.getId(), userId);
                })
                .toList();
    }

    public BookOrder getOrderById(Integer id) throws ServiceException {
        return getExistingOrderById(id);
    }

    public void approveOrderById(Integer id) throws ServiceException {
        advanceOrderStateById(id, OrderState.APPROVED);
    }

    public void declineOrderById(Integer id) throws ServiceException {
        advanceOrderStateById(id, OrderState.DECLINED);
    }

    public void collectOrderById(Integer id) throws ServiceException {
        advanceOrderStateById(id, OrderState.BOOK_TAKEN);
    }

    public void returnOrderById(Integer id) throws ServiceException {
        BookOrder targetOrder = getExistingOrderById(id);
        LocalDate currentDate = LocalDate.now();
        targetOrder.setReturnDate(currentDate);
        orderRepository.save(targetOrder);

        advanceOrderStateById(id, OrderState.BOOK_RETURNED);
    }

    public void advanceOrderStateById(Integer id, OrderState newState) throws ServiceException {
        BookOrder targetOrder = getExistingOrderById(id);
        Book targetOrderBook = targetOrder.getBook();
        Integer targetOrderBookAmount = targetOrderBook.getAmount();

        OrderState currentState = targetOrder.getState();
        if (currentState == OrderState.DECLINED || currentState == OrderState.BOOK_RETURNED) {
            throw new ServiceException("Cannot change the state of order by id = " + id + " from " + currentState + " to " + newState + ": the order has already reaches the end of its lifecycle");
        }
        if (currentState.compareTo(newState) >= 0) {
            throw new ServiceException("Cannot change the state of order by id = " + id + " from " + currentState + " to " + newState + ": that would regress its state");
        }
        if (newState == OrderState.DECLINED && currentState == OrderState.APPROVED) {
            throw new ServiceException("Cannot decline the approved order by id = " + id);
        }
        if (newState == OrderState.BOOK_RETURNED && currentState != OrderState.BOOK_TAKEN) {
            throw new ServiceException("Cannot change the state of the order by id = " + id + ": The book has not been taken yet, it cannot yet be returned");
        }
        if (newState == OrderState.APPROVED && targetOrderBookAmount <= 0) {
            throw new ServiceException("Cannot approve the order by id = " + id + ": The book is not in stock");
        }

        switch (newState) {
            case BOOK_RETURNED -> {
                targetOrderBook.setAmount(targetOrderBookAmount + 1);
                bookRepository.save(targetOrderBook);
            }
            case APPROVED -> {
                targetOrderBook.setAmount(targetOrderBookAmount - 1);
                bookRepository.save(targetOrderBook);
            }
        }

        targetOrder.setState(newState);
        orderRepository.save(targetOrder);
    }

    private BookOrder getExistingOrderById(Integer id) throws ServiceException {
        Optional<BookOrder> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ServiceException("Could not find an order by id = " + id);
        }
        return orderOptional.get();
    }

}
