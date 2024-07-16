package org.speer.assessment.services;

import org.speer.assessment.controllers.CustomerNoteMapper;
import org.speer.assessment.dtos.NoteDto;
import org.speer.assessment.dtos.NoteFilter;
import org.speer.assessment.dtos.ShareNoteDto;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.speer.assessment.exceptions.NotAuthorOperationException;
import org.speer.assessment.repositories.NoteRepository;
import org.speer.assessment.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {
    private final CustomerNoteMapper mapper;

    private final NoteRepository repo;

    private final UserRepository userRepo;

    public NoteService(CustomerNoteMapper mapper, NoteRepository repo, UserRepository userRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public Page<NoteDto> getAll(NoteFilter filter, Pageable pageable) {
        return repo.findAll(filter.toPredicate(), pageable)
                .map(NoteDto::new);
    }

    @Transactional(readOnly = true)
    public Note getNote(Long id, User author) {
        Note note = repo.getReferenceById(id);
        if (!note.getAuthor().equals(author))
            throw new NotAuthorOperationException();
        return note;
    }

    @Transactional
    public Note createNote(NoteDto noteDto, User author) {
        Note note = new Note();
        mapper.updateCustomerFromReq(noteDto, note);
        note.setAuthor(author);
        return repo.save(note);
    }

    @Transactional
    public Note updateNote(NoteDto noteDto, User author) {
        Note note = repo.getReferenceById(noteDto.getId());
        if (!note.getAuthor().equals(author))
            throw new NotAuthorOperationException();
        mapper.updateCustomerFromReq(noteDto, note);
        return repo.save(note);
    }

    @Transactional
    public void deleteNote(Long id, User author) {
        Note note = repo.getReferenceById(id);
        if (!note.getAuthor().equals(author))
            throw new NotAuthorOperationException();
        repo.deleteById(id);
    }

    @Transactional
    public Note shareNote(Long id, ShareNoteDto dto, User author) {
        Note note = repo.getReferenceById(id);
        if (!note.getAuthor().equals(author))
            throw new NotAuthorOperationException();
        User toShare = userRepo.getReferenceById(dto.getShareUserId());
        note.getShareList().add(toShare);
        return repo.save(note);
    }
}
