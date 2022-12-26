package com.patiun.libraryspring.order;

import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.utility.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookOrderController {

    private static final Integer ORDERS_PER_PAGE = 5;

    private final BookOrderService orderService;
    private final Paginator<BookOrder> orderPaginator;

    @Autowired
    public BookOrderController(BookOrderService orderService, Paginator<BookOrder> orderPaginator) {
        this.orderService = orderService;
        this.orderPaginator = orderPaginator;
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestParam("book-id") Integer bookId, @RequestParam RentalType type, @RequestParam(defaultValue = "0") Integer days, final Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();

        orderService.createOrder(bookId, currentUserId, type, days);

        return "redirect:/books";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(defaultValue = "1") Integer page, final Model model, final Authentication authentication) {
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

        List<BookOrder> ordersOfTheTargetPage = orderPaginator.getEntitiesOfPage(orders, page, ORDERS_PER_PAGE);
        int pagesNeededToContainAllOrders = orderPaginator.getNumberOfPagesToContainEntities(orders, ORDERS_PER_PAGE);
        int acceptableTargetPage = orderPaginator.getClosestAcceptableTargetPage(orders, page, ORDERS_PER_PAGE);

        model.addAttribute("orders", ordersOfTheTargetPage);
        model.addAttribute("currentPage", acceptableTargetPage);
        model.addAttribute("maxPage", pagesNeededToContainAllOrders);

        return "orders";
    }
}
