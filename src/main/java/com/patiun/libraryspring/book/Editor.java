package com.patiun.libraryspring.book;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "editor", uniqueConstraints = @UniqueConstraint(columnNames = {"role", "name"}))
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "role", length = 64, nullable = false)
    private String role;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    public Editor() {
    }

    public Editor(Integer id, String role, String name) {
        this.id = id;
        this.role = role;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Editor editor = (Editor) o;
        return Objects.equals(id, editor.id) && Objects.equals(role, editor.role) && Objects.equals(name, editor.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(name);
        return result;
    }

    @Override
    public String toString() {
        return "Editor{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
