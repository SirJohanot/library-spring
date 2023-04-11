package com.patiun.libraryspring.order;

import com.patiun.libraryspring.book.Author;
import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.book.Genre;
import com.patiun.libraryspring.book.Publisher;
import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookOrderRepositoryTest {

    private final TestEntityManager entityManager;

    private final BookOrderRepository orderRepository;

    @Autowired
    public BookOrderRepositoryTest(TestEntityManager entityManager, BookOrderRepository orderRepository) {
        this.entityManager = entityManager;
        this.orderRepository = orderRepository;
    }

    @Test
    public void testFindByUserIdShouldReturnAListOfOrdersOfUserWithIdWhenSuchOrdersExist() {
        //given
        User targetUser = new User(null, "dok", "shgdfdgsa", "john", "smith", false, UserRole.READER);
        Integer targetUserId = entityManager.persistAndGetId(targetUser, Integer.class);

        User secondaryUser = new User(null, "login2", "ihiuehgiwreg2", "fi2rstName", "lastN2ame", false, UserRole.READER);
        entityManager.persist(secondaryUser);

        Book firstBook = new Book(null, "book2", List.of(new Author("author1")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, 7, false);
        entityManager.persist(firstBook);

        Book secondBook = new Book(null, "book3", List.of(new Author("author2")), new Genre(null, "nice genre"), new Publisher(null, "publisher3"), 2000, 65, false);
        entityManager.persist(secondBook);

        entityManager.flush();

        BookOrder firstOrder = new BookOrder(null, firstBook, targetUser, RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 3, 22), LocalDate.of(2023, 3, 29), null, OrderState.PLACED);
        entityManager.persist(firstOrder);

        entityManager.persist(new BookOrder(null, secondBook, secondaryUser, RentalType.TO_READING_HALL, LocalDate.now(), LocalDate.now(), LocalDate.now(), OrderState.BOOK_RETURNED));

        BookOrder thirdOrder = new BookOrder(null, secondBook, targetUser, RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 8), null, OrderState.BOOK_TAKEN);
        entityManager.persist(thirdOrder);

        entityManager.flush();
        //when
        List<BookOrder> actualResult = orderRepository.findByUserId(targetUserId);
        //then
        assertThat(actualResult)
                .hasSize(2)
                .contains(firstOrder, thirdOrder);
    }

    @Test
    public void testFindByUserIdShouldReturnAnEmptyListWhenSuchOrdersDoNotExist() {
        //given
        User firstUser = new User(null, "dok", "shgdfdgsa", "john", "smith", false, UserRole.READER);
        entityManager.persist(firstUser);

        User secondaryUser = new User(null, "login2", "ihiuehgiwreg2", "fi2rstName", "lastN2ame", false, UserRole.READER);
        entityManager.persist(secondaryUser);

        Book firstBook = new Book(null, "book2", List.of(new Author("author1")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, 7, false);
        entityManager.persist(firstBook);

        Book secondBook = new Book(null, "book3", List.of(new Author("author2")), new Genre(null, "nice genre"), new Publisher(null, "publisher3"), 2000, 65, false);
        entityManager.persist(secondBook);

        entityManager.flush();

        entityManager.persist(new BookOrder(null, firstBook, firstUser, RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 3, 22), LocalDate.of(2023, 3, 29), null, OrderState.PLACED));

        entityManager.persist(new BookOrder(null, secondBook, secondaryUser, RentalType.TO_READING_HALL, LocalDate.now(), LocalDate.now(), LocalDate.now(), OrderState.BOOK_RETURNED));
        
        entityManager.persist(new BookOrder(null, secondBook, firstUser, RentalType.OUT_OF_LIBRARY, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 8), null, OrderState.BOOK_TAKEN));

        entityManager.flush();
        //when
        List<BookOrder> actualResult = orderRepository.findByUserId(20);
        //then
        assertThat(actualResult)
                .isEmpty();
    }

}
