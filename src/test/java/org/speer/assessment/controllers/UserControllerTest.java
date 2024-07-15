package org.speer.assessment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speer.assessment.entities.User;
import org.speer.assessment.repositories.UserRepository;
import org.speer.assessment.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository repo;

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
    public void setup() {
        setUpMockSecurityContext();
    }

    @Test
    public void getUserById() throws Exception {
        User mockUser = new User(1L, "test@email.com", "password", "Adam");
        when(repo.getReferenceById(1L)).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.name").value("Adam"));

        verify(repo).getReferenceById(mockUser.getId());

        when(repo.getReferenceById(2L)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
//        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.getById(2L));
    }
}
