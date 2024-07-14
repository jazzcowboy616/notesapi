package org.speer.assessment.controllers;

import org.speer.assessment.annotations.MyRateLimiter;
import org.speer.assessment.dtos.NoteDto;
import org.speer.assessment.dtos.NoteFilter;
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
    private NoteRepository repo;

    @Autowired
    private NoteService service;

    @GetMapping("/search")
    public Page<NoteDto> getAll(
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
    public Page<NoteDto> getNoteList(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        NoteFilter filter = new NoteFilter(null, me);
        return service.getAll(filter, pageable);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @GetMapping("/notes/{id}")
    public NoteDto getNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        Note note = service.getNote(id, me);
        return new NoteDto(note);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @PostMapping("/notes")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createNote(@RequestBody NoteDto noteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        service.createNote(noteDto, me);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @PutMapping("/notes")
    public void updateNote(@RequestBody NoteDto noteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        service.updateNote(noteDto, me);
    }

    @MyRateLimiter(value = "0.1", timeout = "1")
    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        service.deleteNote(id, me);
    }

}
