package com.patiun.libraryspring.order;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookOrderRepository extends ListCrudRepository<BookOrder, Integer> {

    List<BookOrder> findByUserId(Integer id);
}
