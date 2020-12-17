package com.daayCyclic.servletManager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProcedureDto implements ObjectDto {

    private Integer id;
    private String title;
    private String description;
    private Set<String> competencies;

    public ProcedureDto(Integer id, String title, String description, Set<String> competencies) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.competencies = competencies;
    }

    public ProcedureDto(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.competencies = new LinkedHashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcedureDto)) return false;
        ProcedureDto that = (ProcedureDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(competencies, that.competencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, competencies);
    }

    @JsonIgnore
    public boolean isAllNull() {
        return id == null && title == null && description == null && competencies == null;
    }

    public boolean containsAllRequiredValue() {
        return title != null;
    }

    @JsonIgnore
    public boolean isIdValid() {
        return id == null || id >= 0;
    }
}
