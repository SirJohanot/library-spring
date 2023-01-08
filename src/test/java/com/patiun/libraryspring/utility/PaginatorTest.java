package com.patiun.libraryspring.utility;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PaginatorTest {

    private final Paginator<Integer> paginator = new Paginator<>();

    @Test
    public void whenGetNumberOfPagesToContainEntities_thenReturnTheCorrectNumberOfPages() {
        //given
        List<Integer> entities = IntStream.range(1, 13)
                .boxed()
                .toList();
        int entitiesPerPage = 5;
        int expectedNumberOfPagesToContainEntities = 3;
        //when
        int actualNumberOfPagesToContainEntities = paginator.getNumberOfPagesToContainEntities(entities, entitiesPerPage);
        //then
        assertThat(actualNumberOfPagesToContainEntities).isEqualTo(expectedNumberOfPagesToContainEntities);
    }

    @Test
    public void whenGetEntitiesOfPage_thenReturnTheCorrectSublist() {
        //given
        List<Integer> allEntities = IntStream.range(1, 13)
                .boxed()
                .toList();
        int entitiesPerPage = 5;
        int targetPage = 3;
        List<Integer> expectedEntitiesOfTargetPage = IntStream.range(11, 13)
                .boxed()
                .toList();
        //when
        List<Integer> actualEntitiesOfTargetPage = paginator.getEntitiesOfPage(allEntities, targetPage, entitiesPerPage);
        //then
        assertThat(actualEntitiesOfTargetPage).isEqualTo(expectedEntitiesOfTargetPage);
    }

    @Test
    public void whenGetClosestAcceptableTargetPage_thenReturnTheCorrectPageNumber() {
        //given
        List<Integer> allEntities = IntStream.range(1, 7)
                .boxed()
                .toList();
        int entitiesPerPage = 5;
        int targetPage = 2;
        int expectedClosestPage = 2;
        //when
        int actualClosestPage = paginator.getClosestAcceptableTargetPage(allEntities, targetPage, entitiesPerPage);
        //then
        assertThat(actualClosestPage).isEqualTo(expectedClosestPage);
    }

}
