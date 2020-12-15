package com.daayCyclic.servletManager.dao;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity(name = "users")
public class UserDao implements ObjectDao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String surname;
    @Column(nullable = false) private LocalDate dateOfBirth;

    @ManyToOne private RoleDao role;

    @ManyToMany
    @JoinTable(
            name = "users_competencies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "competency_id"))
    private Set<CompetencyDao> competencies;

    @UpdateTimestamp private LocalDateTime lastMod;
    @CreationTimestamp private LocalDateTime timestamp;

    // Constructors
    public UserDao() {}

    public UserDao(Integer user_id, String name, String surname, LocalDate dateOfBirth, RoleDao role) {
        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }
    // End constructors

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public Set<CompetencyDao> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(Set<CompetencyDao> competencies) {
        this.competencies = competencies;
    }

    @Override
    public String toString() {
        return "UserDao{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDao)) return false;
        UserDao userDao = (UserDao) o;
        return user_id.equals(userDao.user_id) &&
                Objects.equals(name, userDao.name) &&
                Objects.equals(surname, userDao.surname) &&
                Objects.equals(dateOfBirth, userDao.dateOfBirth) &&
                Objects.equals(role, userDao.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, name, surname, dateOfBirth, role);
    }

    public boolean isMaintainer() {
        return this.role != null && this.role.getName().compareToIgnoreCase("Maintainer") == 0;
    }
}
