package com.daayCyclic.servletManager.dto;

import com.daayCyclic.servletManager.dao.RoleDao;
import java.time.LocalDate;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "UserDto{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return user_id.equals(userDto.user_id) &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(surname, userDto.surname) &&
                Objects.equals(dateOfBirth, userDto.dateOfBirth) &&
                Objects.equals(role, userDto.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, name, surname, dateOfBirth, role);
    }
}
