package org.speer.assessment.exceptions;

public class NotAuthorOperationException extends RuntimeException {
    public NotAuthorOperationException() {
        super("Only author can perform this operation.");
    }
}
