package org.speer.assessment.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.speer.assessment.entities.User;
import org.speer.assessment.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class UserControllerTest {
    @Mock
    private UserRepository repo;
    private UserController controller;

    @BeforeEach
    public void setup() {
        controller = new UserController(repo);
    }

    @Test
    public void getUserById() {
        User mockUser = new User(1L, "test@email.com", "password", "Adam");
        Mockito.when(repo.getReferenceById(1L)).thenReturn(mockUser);

        ResponseEntity<User> user = controller.getById(mockUser.getId());
        Mockito.verify(repo).getReferenceById(mockUser.getId());
        Assertions.assertEquals(mockUser.getName(), user.getBody().getName());
        Assertions.assertEquals(mockUser.getEmail(), user.getBody().getEmail());
        Assertions.assertEquals(mockUser.getPassword(), user.getBody().getPassword());

        Mockito.when(repo.getReferenceById(2L)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> controller.getById(2L));
    }
}
