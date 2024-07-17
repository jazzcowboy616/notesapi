package org.speer.assessment.controllers;

import org.speer.assessment.annotations.MyRateLimiter;
import org.speer.assessment.entities.User;
import org.speer.assessment.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Get an user by id
     *
     * @param id
     * @return
     */
    @MyRateLimiter(value = "${web.ratelimiter.qps}", timeout = "${web.ratelimiter.timeout}")
    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(repo.getReferenceById(id));
    }
}
