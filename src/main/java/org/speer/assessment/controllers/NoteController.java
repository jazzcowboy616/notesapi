package org.speer.assessment.controllers;

import jakarta.validation.Valid;
import org.speer.assessment.annotations.MyRateLimiter;
import org.speer.assessment.dtos.NoteDto;
import org.speer.assessment.dtos.NoteFilter;
import org.speer.assessment.dtos.ShareNoteDto;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.speer.assessment.services.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    /**
     * search for notes based on keywords for the authenticated user.
     * Only return the contents created by or shared to current user
     * GET /api/search?search={query}
     *
     * @param filter    search={query}
     * @param pageable
     * @param assembler
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @GetMapping("/search")
    public PagedModel<NoteDto> getAll(
            NoteFilter filter,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable, PagedResourcesAssembler assembler
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        filter.setAuthor(me);
        Page<NoteDto> notes = service.getAll(filter, pageable);
        return assembler.toModel(notes);
    }

    /**
     * Get a list of all notes for the authenticated user.
     * Only return the contents created by or shared to current user
     * @param pageable
     * @param assembler
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @GetMapping("/notes")
    public PagedModel getNoteList(@PageableDefault(sort = "id", direction = Sort.Direction.ASC)
                                  Pageable pageable, PagedResourcesAssembler assembler) {
        return getAll(new NoteFilter(), pageable, assembler);
    }

    /**
     * Get a note by ID for the authenticated user.
     * @param id
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteDto> getNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        Note note = service.getNote(id, me);
        return ResponseEntity.ok(new NoteDto(note));
    }

    /**
     * Create a new note for the authenticated user.
     * title and content are mandatory
     * @param noteDto
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @PostMapping("/notes")
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        Note note = service.createNote(noteDto, me);
        return new ResponseEntity(new NoteDto(note), HttpStatus.CREATED);
    }

    /**
     * Update an existing note by ID for the authenticated user.
     * @param id
     * @param noteDto
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @PutMapping("/notes/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto) {
        noteDto.setId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        Note note = service.updateNote(noteDto, me);
        return ResponseEntity.ok(new NoteDto(note));
    }

    /**
     * Delete a note by ID for the authenticated user.
     * @param id
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        service.deleteNote(id, me);
    }

    /**
     * Share a note with another user's ID for the authenticated user.
     * @param id
     * @param dto
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @PostMapping("/notes/{id}/share")
    public void shareNote(@PathVariable Long id, @Valid @RequestBody ShareNoteDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        service.shareNote(id, dto, me);
    }

}
