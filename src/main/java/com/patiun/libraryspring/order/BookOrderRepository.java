package com.patiun.libraryspring.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookOrderRepository extends CrudRepository<BookOrder, Integer> {
}
