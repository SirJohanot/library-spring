package com.patiun.libraryspring.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patiun.libraryspring.validation.Regexp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Login must not be blank")
    @Column(name = "login", length = 64, unique = true)
    private String login;

    @NotBlank(message = "Password must not be blank")
    @Column(name = "password", length = 64)
    @JsonIgnore
    private String password;

    @NotBlank(message = "First name must not be blank")
    @Pattern(regexp = Regexp.HUMAN_NAME, message = "First name must start with an alphabetical character")
    @Column(name = "first_name", length = 64)
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Pattern(regexp = Regexp.HUMAN_NAME, message = "Last name must start with an alphabetical character")
    @Column(name = "last_name", length = 64)
    private String lastName;

    @NotNull(message = "Blocked must not be null")
    @Column(name = "is_blocked")
    private boolean isBlocked;

    @NotNull(message = "Role must not be null")
    @Column(name = "role", length = 64)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User() {
    }

    public User(Integer id, String login, String password, String firstName, String lastName, boolean isBlocked, UserRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.isBlocked = isBlocked;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getLogin();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !isBlocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!Objects.equals(id, user.id)) {
            return false;
        }
        if (!Objects.equals(login, user.login)) {
            return false;
        }
        if (!Objects.equals(password, user.password)) {
            return false;
        }
        if (!Objects.equals(firstName, user.firstName)) {
            return false;
        }
        if (!Objects.equals(lastName, user.lastName)) {
            return false;
        }
        if (!Objects.equals(isBlocked, user.isBlocked)) {
            return false;
        }
        return role == user.role;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (isBlocked ? 1 : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isBlocked=" + isBlocked +
                ", role=" + role +
                '}';
    }
}
