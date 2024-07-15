package org.speer.assessment.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speer.assessment.dtos.NoteDto;
import org.speer.assessment.dtos.NoteFilter;
import org.speer.assessment.dtos.ShareNoteDto;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.speer.assessment.services.JwtService;
import org.speer.assessment.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private PagedResourcesAssembler<NoteDto> assembler;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    public void setUpMockSecurityContext() {
        UserDetails userDetails = new User(1L, "jone@speer.com", "111", "Jone");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    public void setUp() {
        setUpMockSecurityContext();
    }

    @Test
    public void testGetNoteById() throws Exception {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        NoteDto dto = new NoteDto();
        dto.setTitle("Test Note");
        dto.setContent("This is a test note.");
        Note note = new Note();
        note.setId(1L);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setAuthor(author);


        when(noteService.getNote(1L, author)).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/notes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.content").value("This is a test note."));

        when(noteService.getNote(2L, author)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/notes/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateNote() throws Exception {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        NoteDto dto = new NoteDto();
        dto.setTitle("Test Note");
        dto.setContent("This is a test note.");
        Note note = new Note();
        note.setId(1L);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setAuthor(author);

        when(noteService.createNote(dto, author)).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Note\", \"content\":\"This is a test note.\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.content").value("This is a test note."));
    }

    @Test
    public void testUpdateNote() throws Exception {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        NoteDto dto = new NoteDto();
        dto.setId(1L);
        dto.setTitle("Updated Note");
        dto.setContent("This is an updated test note.");
        Note note = new Note();
        note.setId(1L);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setAuthor(author);

        when(noteService.updateNote(dto, author)).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Note\", \"content\":\"This is an updated test note.\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Note"))
                .andExpect(jsonPath("$.content").value("This is an updated test note."));
    }

    @Test
    public void testDeleteNote() throws Exception {
        User author = new User(1L, "jone@speer.com", "111", "Jone");

        doNothing().when(noteService).deleteNote(1L, author);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/notes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(noteService, times(1)).deleteNote(1L, author);
    }

    @Test
    public void testShareNote() throws Exception {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        ShareNoteDto dto = new ShareNoteDto(2L);

        doNothing().when(noteService).shareNote(1L, dto, author);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/notes/1/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"shareUserId\":2}"))
                .andExpect(status().isOk());
        verify(noteService, times(1)).shareNote(1L, dto, author);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/notes/1/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchNote() throws Exception {
        User author = new User(1L, "jone@speer.com", "111", "Jone");
        NoteFilter filter = new NoteFilter("search=note", author);

        NoteDto note = new NoteDto();
        note.setId(1L);
        note.setTitle("title1");
        note.setContent("content1");
        note.setAuthor("Jone");
        Pageable pageable = PageRequest.of(0, 10);
        Page<NoteDto> notes = new PageImpl<>(Collections.singletonList(note), pageable, 1);
        PagedModel<NoteDto> pagedModel = PagedModel.of(Collections.singletonList(note), new PagedModel.PageMetadata(1, 0, 1));

        when(noteService.getAll(any(NoteFilter.class), any(Pageable.class))).thenReturn(notes);
        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/search?search=note")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.noteDtoList[0]").exists());
    }

    @Test
    public void testGetNotes() throws Exception {
        NoteDto note = new NoteDto();
        note.setId(1L);
        note.setTitle("title1");
        note.setContent("content1");
        note.setAuthor("Jone");
        Pageable pageable = PageRequest.of(0, 10);
        Page<NoteDto> notes = new PageImpl<>(Collections.singletonList(note), pageable, 1);
        PagedModel<NoteDto> pagedModel = PagedModel.of(Collections.singletonList(note), new PagedModel.PageMetadata(1, 0, 1));

        when(noteService.getAll(any(NoteFilter.class), any(Pageable.class))).thenReturn(notes);
        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.noteDtoList[0]").exists());
    }
}
