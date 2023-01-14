package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.book.BookRepository;
import com.patiun.libraryspring.exception.ElementNotFoundException;
import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BookOrderServiceImpl implements BookOrderService {

    private final BookOrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookOrderServiceImpl(BookOrderRepository orderRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void createOrder(Integer bookId, Integer userId, RentalType type, Integer days) throws ServiceException {
        User orderingUser = new User();
        orderingUser.setId(userId);

        Book orderedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException("Could not find a book by id = " + bookId));
        if (orderedBook.isDeleted() || orderedBook.getAmount() <= 0) {
            throw new ServiceException("Could not place an order on book by id = " + bookId + ": the book's either deleted or not in stock");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        BookOrder newOrder = new BookOrder(null, orderedBook, orderingUser, type, startDate, endDate, null, OrderState.PLACED);

        orderRepository.save(newOrder);
    }

    @Override
    public List<BookOrder> getAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public List<BookOrder> getOrdersOfUser(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public BookOrder getOrderById(Integer id) {
        return getExistingOrderById(id);
    }

    @Override
    public void approveOrderById(Integer id) throws ServiceException {
        advanceOrderStateById(id, OrderState.APPROVED);
    }

    @Override
    public void declineOrderById(Integer id) throws ServiceException {
        advanceOrderStateById(id, OrderState.DECLINED);
    }

    @Override
    public void collectOrderById(Integer id) throws ServiceException {
        advanceOrderStateById(id, OrderState.BOOK_TAKEN);
    }

    @Override
    public void returnOrderById(Integer id) throws ServiceException {
        BookOrder targetOrder = getExistingOrderById(id);
        LocalDate currentDate = LocalDate.now();
        targetOrder.setReturnDate(currentDate);
        orderRepository.save(targetOrder);

        advanceOrderStateById(id, OrderState.BOOK_RETURNED);
    }

    private void advanceOrderStateById(Integer id, OrderState newState) throws ServiceException {
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

    private BookOrder getExistingOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Could not find an order by id = " + id));
    }

}
