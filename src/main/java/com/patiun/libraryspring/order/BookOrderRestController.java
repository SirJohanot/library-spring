package com.patiun.libraryspring.order;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.user.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false")
public class BookOrderRestController {

    private final BookOrderService orderService;

    @Autowired
    public BookOrderRestController(BookOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders/{bookId}")
    public void placeOrder(@PathVariable Integer bookId, @RequestBody @Valid BookOrderDto orderDto, final Authentication authentication) throws ServiceException {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();

        RentalType type = orderDto.getRentalType();
        Integer days = orderDto.getDays();
        orderService.createOrder(bookId, currentUserId, type, days);
    }

    @GetMapping("/orders")
    public List<BookOrder> getAllOrders(final Authentication authentication) {
        List<BookOrder> orders;
        User currentUser = (User) authentication.getPrincipal();
        switch (currentUser.getRole()) {
            case LIBRARIAN -> orders = orderService.getAllOrders();
            case READER -> {
                Integer readerId = currentUser.getId();
                orders = orderService.getOrdersOfUser(readerId);
            }
            default -> throw new UnsupportedOperationException("Users of your role are not allowed to view orders!");
        }
        return orders;
    }

    @PostMapping("/orders/{id}/approve")
    public void approveOrder(@PathVariable Integer id) throws ServiceException {
        orderService.approveOrderById(id);
    }

    @PostMapping("/orders/{id}/decline")
    public void declineOrder(@PathVariable Integer id) throws ServiceException {
        orderService.declineOrderById(id);
    }

    @PostMapping("/orders/{id}/collect")
    public void collectOrder(@PathVariable Integer id, final Authentication authentication) throws ServiceException {
        if (orderDoesNotBelongToTheAuthenticatedUser(id, authentication)) {
            throw new UnsupportedOperationException("You cannot collect an order which is not yours");
        }

        orderService.collectOrderById(id);
    }

    @PostMapping("/orders/{id}/return")
    public void returnOrder(@PathVariable Integer id, final Authentication authentication) throws ServiceException {
        if (orderDoesNotBelongToTheAuthenticatedUser(id, authentication)) {
            throw new UnsupportedOperationException("You cannot return an order which is not yours");
        }

        orderService.returnOrderById(id);
    }

    @GetMapping("/orders/{id}")
    public BookOrder getOrder(@PathVariable Integer id, final Authentication authentication) {
        BookOrder targetOrder = orderService.getOrderById(id);
        User targetOrderUser = targetOrder.getUser();
        Integer targetOrderUserId = targetOrderUser.getId();

        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();
        UserRole currentUserRole = currentUser.getRole();
        if (!(currentUserRole == UserRole.READER && Objects.equals(targetOrderUserId, currentUserId) || currentUserRole == UserRole.LIBRARIAN)) {
            throw new UnsupportedOperationException("You do not have sufficient privileges to view this order");
        }
        return targetOrder;
    }

    private boolean orderDoesNotBelongToTheAuthenticatedUser(final Integer orderId, final Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();

        BookOrder targetOrder = orderService.getOrderById(orderId);
        User targetOrderUser = targetOrder.getUser();
        Integer targetOrderUserId = targetOrderUser.getId();

        return !currentUserId.equals(targetOrderUserId);
    }

}
