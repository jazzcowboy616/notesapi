package org.speer.assessment.controllers;

import org.speer.assessment.annotations.MyRateLimiter;
import org.speer.assessment.dtos.NoteFilter;
import org.speer.assessment.dtos.NoteOutputDto;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.speer.assessment.repositories.NoteRepository;
import org.speer.assessment.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NoteController {
    @Autowired
    private CustomerNoteMapper mapper;

    @Autowired
    private NoteRepository repo;

    @Autowired
    private NoteService service;

    @GetMapping("/search")
    public Page<NoteOutputDto> getAll(
            NoteFilter filter,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        filter.setAuthor(me);
        return service.getAll(filter, pageable);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @GetMapping("/notes")
    public Page<NoteOutputDto> getNoteList(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        NoteFilter filter = new NoteFilter(null, me);
        return service.getAll(filter, pageable);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @GetMapping("/notes/{id}")
    public NoteOutputDto getNote(@PathVariable Long id) {
        Note note = repo.findById(id).get();
        return new NoteOutputDto(note);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @PostMapping("/notes")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNote(@RequestBody Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        note.setAuthor(me);
        // do more thing, text indexing
        repo.save(note);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @PutMapping("/notes")
    public void updateNote(@RequestBody Note note) {
        Note myNote = repo.getReferenceById(note.getId());
        mapper.updateCustomerFromReq(note, myNote);
        // refresh text indexing
        repo.save(myNote);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable Long id) {
        repo.deleteById(id);
    }

}
