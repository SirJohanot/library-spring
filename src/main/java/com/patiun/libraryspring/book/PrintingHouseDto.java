package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class PrintingHouseDto {

    @NotNull(message = "Printing house name must not be null")
    @Pattern(regexp = Regexp.WORD, message = "Publisher name must start with an alphabetical character or a number")
    private String name;

    @NotNull(message = "Printing house postal code must not be null")
    @Pattern(regexp = Regexp.WORD, message = "Publisher postal code must start with an alphabetical character or a number")
    private String postalCode;

    @NotNull(message = "Printing house address must not be null")
    @Pattern(regexp = Regexp.WORD, message = "Publisher address must start with an alphabetical character or a number")
    private String address;

    public PrintingHouseDto() {
    }

    public PrintingHouseDto(String name, String postalCode, String address) {
        this.name = name;
        this.postalCode = postalCode;
        this.address = address;
    }

    public @NotNull(message = "Printing house name must not be null") @Pattern(regexp = Regexp.WORD, message = "Publisher name must start with an alphabetical character or a number") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Printing house name must not be null") @Pattern(regexp = Regexp.WORD, message = "Publisher name must start with an alphabetical character or a number") String name) {
        this.name = name;
    }

    public @NotNull(message = "Printing house postal code must not be null") @Pattern(regexp = Regexp.WORD, message = "Publisher postal code must start with an alphabetical character or a number") String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@NotNull(message = "Printing house postal code must not be null") @Pattern(regexp = Regexp.WORD, message = "Publisher postal code must start with an alphabetical character or a number") String postalCode) {
        this.postalCode = postalCode;
    }

    public @NotNull(message = "Printing house address must not be null") @Pattern(regexp = Regexp.WORD, message = "Publisher address must start with an alphabetical character or a number") String getAddress() {
        return address;
    }

    public void setAddress(@NotNull(message = "Printing house address must not be null") @Pattern(regexp = Regexp.WORD, message = "Publisher address must start with an alphabetical character or a number") String address) {
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

        PrintingHouseDto that = (PrintingHouseDto) o;
        return Objects.equals(name, that.name) && Objects.equals(postalCode, that.postalCode) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(postalCode);
        result = 31 * result + Objects.hashCode(address);
        return result;
    }

    @Override
    public String toString() {
        return "PrintingHouseDto{" +
                "name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
