package com.daayCyclic.servletManager.converter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Converter<T, U> {

    private final Function<T, U> fromDto;
    private final Function<U, T> fromEntity;

    public Converter(final Function<T, U> fromDto, final Function<U, T> fromEntity) {
        this.fromDto = fromDto;
        this.fromEntity = fromEntity;
    }

    public final U convertFromDto(final T dto) {
        return fromDto.apply(dto);
    }

    public final T convertFromEntity(final U entity) {
        return fromEntity.apply(entity);
    }

    public final Collection<U> createFromDtos(final Collection<T> dtos) {
        if (dtos != null) {
            return dtos.stream().map(this::convertFromDto).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return null;
    }

    public final Collection<T> createFromEntities(final Collection<U> entities) {
        if (entities != null) {
            return entities.stream().map(this::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return null;
    }

}
