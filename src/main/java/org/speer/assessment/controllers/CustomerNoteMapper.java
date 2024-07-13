package org.speer.assessment.controllers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.speer.assessment.entities.Note;

@Mapper(componentModel = "Spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerNoteMapper {
    void updateCustomerFromReq(Note note, @MappingTarget Note entity);
}
