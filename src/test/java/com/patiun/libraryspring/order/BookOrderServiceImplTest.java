package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.*;
import com.patiun.libraryspring.exception.ElementNotFoundException;
import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BookOrderServiceImplTest {

    @Mock
    private BookOrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookOrderServiceImpl bookOrderService;

    @Test
    public void createOrderShouldSaveNewOrderWhenTheBookExists() throws ServiceException {
        //given
        Integer idOfBookToOrder = 4;
        Book bookFoundByRepository = new Book(idOfBookToOrder, "Nice Book", List.of(new Author(1, "Nice Author")), new Genre(1, "Interesting genre"), new Publisher(1, "Competent publisher"), 2004, 54, false);
        given(bookRepository.findById(idOfBookToOrder))
                .willReturn(Optional.of(bookFoundByRepository));

        Integer orderingUserId = 3;
        User expectedUserToBeSaved = new User();
        expectedUserToBeSaved.setId(orderingUserId);

        RentalType type = RentalType.OUT_OF_LIBRARY;

        int orderDays = 7;
        LocalDate expectedStartDate = LocalDate.now();
        LocalDate expectedEndDate = expectedStartDate.plusDays(orderDays);

        BookOrder expectedCreatedOrder = new BookOrder(null, bookFoundByRepository, expectedUserToBeSaved, type, expectedStartDate, expectedEndDate, null, OrderState.PLACED);
        //when
        bookOrderService.createOrder(idOfBookToOrder, orderingUserId, type, orderDays);
        //then
        then(orderRepository)
                .should(times(1))
                .save(expectedCreatedOrder);
    }

    @Test
    public void createOrderShouldThrowServiceExceptionWhenBookDoesNotExist() {
        //given
        Integer idOfBookToOrder = 4;
        given(bookRepository.findById(idOfBookToOrder))
                .willReturn(Optional.empty());

        Integer orderingUserId = 3;
        //then
        assertThatThrownBy(() -> bookOrderService.createOrder(idOfBookToOrder, orderingUserId, RentalType.OUT_OF_LIBRARY, 7))
                .isInstanceOf(ServiceException.class);
    }

    @Test
    public void createOrderShouldThrowServiceExceptionWhenBookIsDeleted() {
        //given
        Integer idOfBookToOrder = 4;
        Book bookFoundByRepository = new Book(idOfBookToOrder, "Nice Book", List.of(new Author(1, "Nice Author")), new Genre(1, "Interesting genre"), new Publisher(1, "Competent publisher"), 2004, 54, true);
        given(bookRepository.findById(idOfBookToOrder))
                .willReturn(Optional.of(bookFoundByRepository));

        Integer orderingUserId = 3;
        //then
        assertThatThrownBy(() -> bookOrderService.createOrder(idOfBookToOrder, orderingUserId, RentalType.OUT_OF_LIBRARY, 7))
                .isInstanceOf(ServiceException.class);
    }

    @Test
    public void createOrderShouldThrowServiceExceptionWhenBookIsNotInStock() {
        //given
        Integer idOfBookToOrder = 4;
        Book bookFoundByRepository = new Book(idOfBookToOrder, "Nice Book", List.of(new Author(1, "Nice Author")), new Genre(1, "Interesting genre"), new Publisher(1, "Competent publisher"), 2004, 0, false);
        given(bookRepository.findById(idOfBookToOrder))
                .willReturn(Optional.of(bookFoundByRepository));

        Integer orderingUserId = 3;
        //then
        assertThatThrownBy(() -> bookOrderService.createOrder(idOfBookToOrder, orderingUserId, RentalType.OUT_OF_LIBRARY, 7))
                .isInstanceOf(ServiceException.class);
    }

    @Test
    public void getAllOrdersShouldReturnAllOrdersFoundByRepository() {
        //given
        List<BookOrder> expectedOrders = Arrays.asList(
                new BookOrder(1, new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false), new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER), RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 3, 22), LocalDate.of(2023, 3, 29), null, OrderState.PLACED),
                new BookOrder(2, new Book(3, "book3", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false), new User(2, "login2", "ihiuehgiwreg2", "fi2rstName", "lastN2ame", false, UserRole.READER), RentalType.TO_READING_HALL, LocalDate.now(), LocalDate.now(), LocalDate.now(), OrderState.BOOK_RETURNED),
                new BookOrder(4, new Book(3, "book3", List.of(new Author(1, "author1")), new Genre(4, "nice genre"), new Publisher(3, "publisher3"), 2000, 65, false), new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER), RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 8), null, OrderState.BOOK_TAKEN)
        );
        given(orderRepository.findAll())
                .willReturn(expectedOrders);
        //when
        List<BookOrder> actualOrders = bookOrderService.getAllOrders();
        //then
        assertThat(actualOrders)
                .isEqualTo(expectedOrders);
    }

    @Test
    public void getOrdersOfUserShouldReturnAllOrdersFoundByRepository() {
        //given
        Integer targetUserId = 1;
        List<BookOrder> expectedOrders = Arrays.asList(
                new BookOrder(1, new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false), new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER), RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 3, 22), LocalDate.of(2023, 3, 29), null, OrderState.PLACED),
                new BookOrder(2, new Book(3, "book3", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false), new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER), RentalType.TO_READING_HALL, LocalDate.now(), LocalDate.now(), LocalDate.now(), OrderState.BOOK_RETURNED),
                new BookOrder(4, new Book(3, "book3", List.of(new Author(1, "author1")), new Genre(4, "nice genre"), new Publisher(3, "publisher3"), 2000, 65, false), new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER), RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 8), null, OrderState.BOOK_TAKEN)
        );
        given(orderRepository.findByUserId(targetUserId))
                .willReturn(expectedOrders);
        //when
        List<BookOrder> actualOrders = bookOrderService.getOrdersOfUser(targetUserId);
        //then
        assertThat(actualOrders)
                .isEqualTo(expectedOrders);
    }

    @Test
    public void getOrderByIdShouldReturnOrderFoundByRepositoryWhenOrderExists() {
        //given
        Integer orderId = 1;
        BookOrder expectedOrder = new BookOrder(orderId, new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false), new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER), RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 3, 22), LocalDate.of(2023, 3, 29), null, OrderState.PLACED);
        given(orderRepository.findById(orderId))
                .willReturn(Optional.of(expectedOrder));
        //when
        BookOrder actualOrder = bookOrderService.getOrderById(orderId);
        //then
        assertThat(actualOrder)
                .isEqualTo(expectedOrder);
    }

    @Test
    public void getOrderByIdShouldThrowElementNotFoundExceptionWhenOrderDoesNotExist() {
        //given
        Integer orderId = 1;
        given(orderRepository.findById(orderId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> bookOrderService.getOrderById(orderId))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void approveOrderByIdShouldDecrementBookAmountAndAdvanceStateWhenOrderCanBeApproved() throws ServiceException {
        //given
        Integer orderId = 1;

        Integer targetBookId = 2;
        String title = "War and Peace";
        List<Author> authors = List.of(new Author(1, "Leo Tolstoy"));
        Genre genre = new Genre(1, "Historical Novel");
        Publisher publisher = new Publisher(2, "Hardcover");
        Integer publishmentYear = 2014;
        int amount = 16;
        Book orderBook = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount, false);
        Book expectedBookToBeSaved = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount - 1, false);

        User orderUser = new User(1, "login", "ihiuehgiwreg", "firstName", "lastName", false, UserRole.READER);

        RentalType type = RentalType.OUT_OF_LIBRARY;
        LocalDate startDate = LocalDate.of(2023, 3, 22);
        LocalDate endDate = LocalDate.of(2023, 3, 29);

        BookOrder targetOrder = new BookOrder(orderId, orderBook, orderUser, type, startDate, endDate, null, OrderState.PLACED);
        given(orderRepository.findById(orderId))
                .willReturn(Optional.of(targetOrder));

        BookOrder expectedOrderToBeSaved = new BookOrder(orderId, orderBook, orderUser, type, startDate, endDate, null, OrderState.APPROVED);
        //when
        bookOrderService.approveOrderById(1);
        //then
        then(bookRepository)
                .should(times(1))
                .save(expectedBookToBeSaved);

        then(orderRepository)
                .should(times(1))
                .save(expectedOrderToBeSaved);
    }

}
