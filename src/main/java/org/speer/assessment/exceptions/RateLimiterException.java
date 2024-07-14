package org.speer.assessment.exceptions;

public class RateLimiterException extends RuntimeException {
    public RateLimiterException() {
        super("Request too frequently, please try later.");
    }
}
