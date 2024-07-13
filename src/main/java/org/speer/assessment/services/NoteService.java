package org.speer.assessment.services;

import org.speer.assessment.dtos.NoteFilter;
import org.speer.assessment.dtos.NoteOutputDto;
import org.speer.assessment.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {
    @Autowired
    private NoteRepository repo;

    @Transactional(readOnly = true)
    public Page<NoteOutputDto> getAll(NoteFilter filter, Pageable pageable) {
        return repo.findAll(filter.toPredicate(), pageable)
                .map(NoteOutputDto::new);
    }
}
