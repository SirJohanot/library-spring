package com.patiun.libraryspring.order;

import com.patiun.libraryspring.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookOrderController {

    private final BookOrderService orderService;

    @Autowired
    public BookOrderController(BookOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestParam("book-id") Integer bookId, @RequestParam RentalType type, @RequestParam(defaultValue = "0") Integer days, final Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();

        orderService.createOrder(bookId, currentUserId, type, days);

        return "redirect:/books";
    }
}
