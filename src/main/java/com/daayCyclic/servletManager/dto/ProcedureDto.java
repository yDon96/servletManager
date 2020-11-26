package com.daayCyclic.servletManager.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcedureDto implements ObjectDto {

    @NotNull
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
        return obj instanceof ProcedureDto
                && ((ProcedureDto) obj).id.equals(id)
                && (((ProcedureDto) obj).title == title)
                && ((ProcedureDto) obj).description.equals(description);
    }
}
