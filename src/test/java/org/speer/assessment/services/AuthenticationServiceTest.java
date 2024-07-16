package org.speer.assessment.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.speer.assessment.dtos.LoginUserDto;
import org.speer.assessment.dtos.RegisterUserDto;
import org.speer.assessment.entities.User;
import org.speer.assessment.exceptions.UserExistingException;
import org.speer.assessment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    @InjectMocks
    private AuthenticationService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        Assertions.assertThrows(UserExistingException.class, () -> service.signup(dto));

        User user = new User();
        user.setName("test");
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        User res = service.signup(dto);

        Assertions.assertEquals(user.getName(), res.getName());
        Assertions.assertEquals(user.getEmail(), res.getEmail());
        Assertions.assertEquals(user.getPassword(), res.getPassword());
    }

    @Test
    public void testLogin() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123456");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> service.authenticate(dto));

        User user = new User();
        user.setName("test");
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User res = service.authenticate(dto);

        Assertions.assertEquals(user.getName(), res.getName());
        Assertions.assertEquals(user.getEmail(), res.getEmail());
        Assertions.assertEquals(user.getPassword(), res.getPassword());

        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(InternalAuthenticationServiceException.class);

        Assertions.assertThrows(AuthenticationException.class, () -> service.authenticate(dto));
    }
}
