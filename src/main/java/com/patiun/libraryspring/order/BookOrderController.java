package com.patiun.libraryspring.order;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.user.UserRole;
import com.patiun.libraryspring.utility.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        return "redirect:/books/";
    }

    @GetMapping({"/orders/", "/orders/{page}"})
    public String orders(@PathVariable("page") Optional<Integer> optionalPage, final Model model, final Authentication authentication) {
        int page = optionalPage.orElse(1);

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

    @PostMapping("/approve-order")
    public String approveOrder(@RequestParam Integer id) throws ServiceException {
        orderService.approveOrderById(id);

        return "redirect:/order/" + id;
    }

    @GetMapping("/order/{id}")
    public String order(@PathVariable Integer id, final Model model, final Authentication authentication) throws ServiceException {
        BookOrder targetOrder = orderService.getOrderById(id);
        User targetOrderUser = targetOrder.getUser();
        Integer targetOrderUserId = targetOrderUser.getId();

        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();
        UserRole currentUserRole = currentUser.getRole();
        if (!(currentUserRole == UserRole.READER && Objects.equals(targetOrderUserId, currentUserId) || currentUserRole == UserRole.LIBRARIAN)) {
            throw new UnsupportedOperationException("You do not have sufficient privileges to view this order");
        }

        model.addAttribute("order", targetOrder);

        return "order";
    }
}
