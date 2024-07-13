package org.speer.assessment.annotations;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    private static final Map<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    @Pointcut("@annotation(org.speer.assessment.annotations.MyRateLimiter)")
    public void rateLimiter() {
    }

    @Around("rateLimiter()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        MyRateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, MyRateLimiter.class);
        assert rateLimiter != null;
        double qps = Double.parseDouble(rateLimiter.value());
        if (qps > MyRateLimiter.NOT_LIMITED_VAL) {
            RATE_LIMITER_CACHE.putIfAbsent(method.getName(), RateLimiter.create(qps));

            if (RATE_LIMITER_CACHE.get(method.getName()) != null
                    && !RATE_LIMITER_CACHE.get(method.getName()).tryAcquire(Integer.parseInt(rateLimiter.timeout()), rateLimiter.timeUnit())) {
                throw new RuntimeException("Request too frequently, please try later.");
            }
        }
        return point.proceed();
    }
}
