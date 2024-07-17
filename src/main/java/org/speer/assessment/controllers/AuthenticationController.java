package org.speer.assessment.controllers;

import jakarta.validation.Valid;
import org.speer.assessment.annotations.MyRateLimiter;
import org.speer.assessment.dtos.LoginUserDto;
import org.speer.assessment.dtos.RegisterUserDto;
import org.speer.assessment.entities.User;
import org.speer.assessment.responses.LoginResponse;
import org.speer.assessment.services.AuthenticationService;
import org.speer.assessment.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * create a new user account.
     *
     * @param registerUserDto
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @PostMapping("/signup")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        if (!registerUserDto.getPassword().equals(registerUserDto.getRepeatPassword()))
            throw new IllegalArgumentException("Re-entered password not consistent to password");
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    /**
     * log in to an existing user account and receive an access token.
     * @param loginUserDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
