package com.patiun.libraryspring.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookOrderRepository extends CrudRepository<BookOrder, Integer> {

    List<BookOrder> findByUserId(Integer id);
}
