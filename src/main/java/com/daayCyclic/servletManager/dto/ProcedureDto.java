package com.daayCyclic.servletManager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureDto implements ObjectDto {

    public Integer id;

    public String title;

    public String description;

    public ProcedureDto(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return  (obj instanceof ProcedureDto)
                && id.equals(((ProcedureDto) obj).id)
                && (Objects.equals(title, ((ProcedureDto) obj).title))
                && (Objects.equals(description, ((ProcedureDto) obj).description));
    }

    public boolean isAllNull() {
        return id == null && title == null && description == null;
    }
}
