package com.daayCyclic.servletManager.dto;

import com.daayCyclic.servletManager.dao.RoleDao;
import java.time.LocalDate;

public class UserDto implements ObjectDto {

    private final Integer user_id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String role;

    public UserDto(Integer user_id, String name, String surname, LocalDate dateOfBirth, String role) {
        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
