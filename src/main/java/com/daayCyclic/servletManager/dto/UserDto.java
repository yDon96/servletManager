package com.daayCyclic.servletManager.dto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class UserDto implements ObjectDto {

    private final Integer userId;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String role;
    private Set<String> competencies;

    public UserDto(Integer userId, String name, String surname, LocalDate dateOfBirth, String role) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
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

    public Set<String> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(Set<String> competencies) {
        this.competencies = competencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(userId, userDto.userId) &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(surname, userDto.surname) &&
                Objects.equals(dateOfBirth, userDto.dateOfBirth) &&
                Objects.equals(role, userDto.role) &&
                Objects.equals(competencies, userDto.competencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, surname, dateOfBirth, role, competencies);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "user_id=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role='" + role + '\'' +
                '}';
    }

}
