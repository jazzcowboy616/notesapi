package org.speer.assessment.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.speer.assessment.controllers.CustomerNoteMapper;
import org.speer.assessment.dtos.NoteDto;
import org.speer.assessment.dtos.NoteFilter;
import org.speer.assessment.dtos.ShareNoteDto;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.speer.assessment.exceptions.NotAuthorOperationException;
import org.speer.assessment.repositories.NoteRepository;
import org.speer.assessment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NoteServiceTest {
    @MockBean
    private CustomerNoteMapper mapper;

    @MockBean
    private NoteRepository repo;

    @MockBean
    private UserRepository userRepo;

    @Autowired
    @InjectMocks
    private NoteService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNote() {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        User me = new User(2L, "mike@speer.com", "111", "Mike");
        Note note = new Note(1L, "test title", "test content");
        note.setAuthor(author);
        when(repo.getReferenceById(1L)).thenReturn(note);

        Assertions.assertThrows(NotAuthorOperationException.class, () -> service.getNote(1L, me));
        Note res = service.getNote(1L, author);
        Assertions.assertEquals(res.getTitle(), note.getTitle());
        Assertions.assertEquals(res.getContent(), note.getContent());
    }

    @Test
    public void testCreateNote() {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("test title");
        noteDto.setContent("test content");
        Note note = new Note(1L, noteDto.getTitle(), noteDto.getContent());
        note.setAuthor(author);
        when(repo.save(any(Note.class))).thenReturn(note);

        Note res = service.createNote(noteDto, author);
        Assertions.assertEquals(author, res.getAuthor());
        Assertions.assertEquals(noteDto.getTitle(), res.getTitle());
        Assertions.assertEquals(noteDto.getContent(), res.getContent());
    }

    @Test
    public void testUpdateNote() {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        User me = new User(2L, "mike@speer.com", "111", "Mike");
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("update title");
        Note note1 = new Note(1L, "test title", "test content");
        note1.setAuthor(author);
        when(repo.getReferenceById(1L)).thenReturn(note1);

        Assertions.assertThrows(NotAuthorOperationException.class, () -> service.updateNote(noteDto, me));

        Note note2 = new Note(1L, noteDto.getTitle(), "test content");
        note2.setAuthor(author);
        when(repo.save(any(Note.class))).thenReturn(note2);
        Note res = service.updateNote(noteDto, author);
        Assertions.assertEquals(author, res.getAuthor());
        Assertions.assertEquals(noteDto.getTitle(), res.getTitle());
        Assertions.assertEquals("test content", res.getContent());
    }

    @Test
    public void testDeleteNote() {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        User me = new User(2L, "mike@speer.com", "111", "Mike");
        Note note1 = new Note(1L, "test title", "test content");
        note1.setAuthor(author);
        when(repo.getReferenceById(1L)).thenReturn(note1);

        Assertions.assertThrows(NotAuthorOperationException.class, () -> service.deleteNote(1L, me));
        doNothing().when(repo).deleteById(1L);
        service.deleteNote(1L, author);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    public void testShareNote() {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        User other = new User(2L, "mike@speer.com", "111", "Mike");
        Note note1 = new Note(1L, "test title", "test content");
        note1.setAuthor(author);
        note1.setShareList(new HashSet<>());
        note1.getShareList().add(other);
        ShareNoteDto dto = new ShareNoteDto();
        dto.setShareUserId(2L);
        when(repo.getReferenceById(1L)).thenReturn(note1);

        Assertions.assertThrows(NotAuthorOperationException.class, () -> service.shareNote(1L, dto, other));

        when(repo.save(any(Note.class))).thenReturn(note1);
        Note res = service.shareNote(1L, dto, author);
        verify(repo, times(1)).save(any(Note.class));
        Assertions.assertEquals(author, res.getAuthor());
        Assertions.assertTrue(res.getShareList().contains(other));
    }

    @Test
    public void testSearchNote() {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        Note note = new Note();
        note.setId(1L);
        note.setTitle("title1");
        note.setContent("content1");
        note.setAuthor(author);
        NoteFilter filter = new NoteFilter("search=test", author);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Note> notes = new PageImpl<>(Collections.singletonList(note), pageable, 1);
        Page<Note> empty = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(repo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(notes);
        Page<NoteDto> res = service.getAll(filter, pageable);
        Assertions.assertEquals(1, res.getContent().size());
        Assertions.assertEquals(note.getTitle(), res.getContent().get(0).getTitle());
        Assertions.assertEquals(note.getContent(), res.getContent().get(0).getContent());

        when(repo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(empty);
        res = service.getAll(filter, pageable);
        Assertions.assertEquals(0, res.getContent().size());
    }
}
