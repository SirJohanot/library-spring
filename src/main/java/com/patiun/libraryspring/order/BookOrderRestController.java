package com.patiun.libraryspring.order;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.user.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class BookOrderRestController {

    private final BookOrderService orderService;

    @Autowired
    public BookOrderRestController(BookOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("{bookId}")
    public void createOrder(@PathVariable Integer bookId, @RequestBody @Valid BookOrderDto orderDto, final Authentication authentication) throws ServiceException {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();

        RentalType type = orderDto.getRentalType();
        Integer days = orderDto.getDays();
        orderService.createOrder(bookId, currentUserId, type, days);
    }

    @GetMapping
    public List<BookOrder> readAllOrders(final Authentication authentication) {
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

    @GetMapping("{id}")
    public BookOrder readOrder(@PathVariable Integer id, final Authentication authentication) {
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

    @PatchMapping("{id}/approve")
    public void approveOrder(@PathVariable Integer id) throws ServiceException {
        orderService.approveOrderById(id);
    }

    @PatchMapping("{id}/decline")
    public void declineOrder(@PathVariable Integer id) throws ServiceException {
        orderService.declineOrderById(id);
    }

    @PatchMapping("{id}/collect")
    public void collectOrder(@PathVariable Integer id) throws ServiceException {
        orderService.collectOrderById(id);
    }

    @PatchMapping("{id}/return")
    public void returnOrder(@PathVariable Integer id) throws ServiceException {
        orderService.returnOrderById(id);
    }
    
}
