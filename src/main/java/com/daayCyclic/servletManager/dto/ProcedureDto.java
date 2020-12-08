package com.daayCyclic.servletManager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureDto implements ObjectDto {

    private Integer id;
    private String title;
    private String description;
    private List<String> competencies;

    public ProcedureDto(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public boolean isAllNull() {
        return id == null && title == null && description == null;
    }

    public boolean containsAllRequiredValue() {
        return title != null;
    }

    public boolean isIdValid() {
        return id == null || id >= 0;
    }
}
