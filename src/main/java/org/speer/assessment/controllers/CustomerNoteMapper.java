package org.speer.assessment.controllers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.speer.assessment.dtos.NoteDto;
import org.speer.assessment.entities.Note;

@Mapper(componentModel = "Spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerNoteMapper {
    @Mapping(target = "author", ignore = true)
    void updateCustomerFromReq(NoteDto note, @MappingTarget Note entity);
}
