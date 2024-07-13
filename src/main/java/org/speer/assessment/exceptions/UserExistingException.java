package org.speer.assessment.exceptions;

public class UserExistingException extends RuntimeException {
    public UserExistingException(String email) {
        super("Email " + email + " has already been used");
    }
}
