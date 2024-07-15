package org.speer.assessment.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.speer.assessment.dtos.LoginUserDto;
import org.speer.assessment.dtos.RegisterUserDto;
import org.speer.assessment.entities.User;
import org.speer.assessment.services.AuthenticationService;
import org.speer.assessment.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void testRegister() throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setPassword("123456");
        dto.setRepeatPassword("123456");
        dto.setEmail("test@test.com");
        dto.setFullName("test");

        User user = new User();
        user.setId(1L);
        user.setName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword("$2a$10$7LBjtBGe9/Val.xOi1EH9ejHIxWcMv0wX3MjpJew8i0h1fTZR1WRy");
        Mockito.when(authenticationService.signup(dto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"test\",\"email\":\"test@test.com\"," +
                                "\"password\": \"123456\",\"repeatPassword\": \"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
        verify(authenticationService, times(1)).signup(dto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"test\",\"email\":\"test@test.com\"," +
                                "\"password\": \"123456\",\"repeatPassword\": \"111111\"}"))
                .andExpect(status().isBadRequest());
        verify(authenticationService, times(1)).signup(dto);
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        String token = "generated-token";
        User user = new User();
        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(user);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(86400000L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"password\": \"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.expiresIn").value(86400000L));

        verify(authenticationService, times(1)).authenticate(any(LoginUserDto.class));
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
        verify(jwtService, times(1)).getExpirationTime();
    }
}
