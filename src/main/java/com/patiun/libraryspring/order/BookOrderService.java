package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Book;
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

    public void advanceOrderStateById(Integer id, OrderState newState) throws ServiceException {
        BookOrder targetOrder = getExistingOrderById(id);

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
