package org.speer.assessment.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.speer.assessment.dtos.RegisterUserDto;
import org.speer.assessment.entities.User;
import org.speer.assessment.services.AuthenticationService;
import org.speer.assessment.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class AuthenticationControllerTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationService authenticationService;
    private AuthenticationController controller;

    @BeforeEach
    public void initController() {
//        jwtService = Mockito.mock(JwtService.class);
//        authenticationService = Mockito.mock(AuthenticationService.class);
        controller = new AuthenticationController(jwtService, authenticationService);
    }

    @Test
    public void registerWithDiffPwd() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setPassword("123456");
        dto.setRepeatPassword("111111");
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.register(dto));
        Mockito.verify(authenticationService, Mockito.never()).signup(dto);
    }

    @Test
    public void register() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setPassword("123456");
        dto.setRepeatPassword("123456");
        dto.setEmail("test@test.com");
        dto.setFullName("test");

        User user = new User();
        user.setName(dto.getFullName());
        user.setEmail(dto.getEmail());
        Mockito.when(authenticationService.signup(dto)).thenReturn(user);

        ResponseEntity<User> res = controller.register(dto);
        Assertions.assertEquals(res.getBody().getEmail(), dto.getEmail());
        Assertions.assertEquals(res.getBody().getName(), dto.getFullName());
    }
}
