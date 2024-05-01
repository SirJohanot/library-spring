package com.patiun.libraryspring.book;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "publisher", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "postal_code", "address"}))
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;

    @Column(name = "address", length = 128, nullable = false)
    private String address;

    public Publisher() {
    }

    public Publisher(Integer id, String name, String postalCode, String address) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Publisher publisher = (Publisher) o;
        return Objects.equals(id, publisher.id) && Objects.equals(name, publisher.name) && Objects.equals(postalCode, publisher.postalCode) && Objects.equals(address, publisher.address);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(postalCode);
        result = 31 * result + Objects.hashCode(address);
        return result;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}