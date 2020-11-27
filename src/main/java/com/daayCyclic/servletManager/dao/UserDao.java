package com.daayCyclic.servletManager.dao;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "users")
public class UserDao implements ObjectDao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    @ManyToOne private RoleDao role;
    @UpdateTimestamp private LocalDateTime lastMod;
    @CreationTimestamp private LocalDateTime timestamp;

    public Integer getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public RoleDao getRole() {
        return role;
    }

    public void setRole(RoleDao role) {
        this.role = role;
    }

    public LocalDateTime getLastMod() {
        return lastMod;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
