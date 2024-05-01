package com.patiun.libraryspring.book;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrintingHouseRepository extends CrudRepository<PrintingHouse, Integer> {

    Optional<PrintingHouse> findByNameAndPostalCodeAndAddress(String name, String postalCode, String address);
}
