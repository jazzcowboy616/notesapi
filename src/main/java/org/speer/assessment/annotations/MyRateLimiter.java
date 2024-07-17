package org.speer.assessment.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * MyRateLimiter is used to add rate limiting on the specific methods.
 * permitsPerSecond: numbers of requests allowed every second
 * timeout: interval of each request to get token
 * timeunit: time unit, second by default
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRateLimiter {
    String NOT_LIMITED = "0";
    double NOT_LIMITED_VAL = 0;

    @AliasFor("permitsPerSecond") String value() default NOT_LIMITED;

    String permitsPerSecond() default NOT_LIMITED;

    String timeout() default NOT_LIMITED;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
