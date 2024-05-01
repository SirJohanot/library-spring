package com.patiun.libraryspring.book;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PublisherRepository extends CrudRepository<Publisher, Integer> {

    Optional<Publisher> findByNameAndPostalCodeAndAddress(String name, String postalCode, String address);
}
