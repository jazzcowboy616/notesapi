package org.speer.assessment.controllers;

import org.speer.assessment.annotations.MyRateLimiter;
import org.speer.assessment.entities.User;
import org.speer.assessment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository repo;

    @MyRateLimiter(value = "0.1", timeout = "1")
    @GetMapping("{id}")
    public User getById(@PathVariable Long id) {
        return repo.getReferenceById(id);
    }
}
