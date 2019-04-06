package org.lance.cloud.resilience.fixtrue;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.configure.CircuitBreakerConfigurationProperties;
import io.github.resilience4j.circuitbreaker.utils.CircuitBreakerUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class EnhanceCircuitBreakerAspect implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(EnhanceCircuitBreakerAspect.class);

    private final CircuitBreakerConfigurationProperties circuitBreakerProperties;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public EnhanceCircuitBreakerAspect(CircuitBreakerConfigurationProperties backendMonitorPropertiesRegistry, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerProperties = backendMonitorPropertiesRegistry;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Pointcut(value = "@within(enhanceCircuitBreaker) || @annotation(enhanceCircuitBreaker)", argNames = "enhanceCircuitBreaker")
    public void matchAnnotatedClassOrMethod(EnhanceCircuitBreaker enhanceCircuitBreaker) {
    }

    @Around(value = "matchAnnotatedClassOrMethod(backendMonitored)", argNames = "proceedingJoinPoint, backendMonitored")
    public Object circuitBreakerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, EnhanceCircuitBreaker backendMonitored) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();
        if (backendMonitored == null) {
            backendMonitored = getBackendMonitoredAnnotation(proceedingJoinPoint);
        }
        String backend = backendMonitored.name();
        // recovery
        RecoveryFunction<?> recoveryFunction = backendMonitored.recovery().newInstance();

        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = getOrCreateCircuitBreaker(methodName, backend);
        return handleJoinPoint(proceedingJoinPoint, circuitBreaker, methodName, recoveryFunction);
    }

    private io.github.resilience4j.circuitbreaker.CircuitBreaker getOrCreateCircuitBreaker(String methodName, String backend) {
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(backend,
                () -> circuitBreakerProperties.createCircuitBreakerConfig(backend));

        if (logger.isDebugEnabled()) {
            logger.debug("Created or retrieved circuit breaker '{}' with failure rate '{}' and wait interval'{}' for method: '{}'",
                    backend, circuitBreaker.getCircuitBreakerConfig().getFailureRateThreshold(),
                    circuitBreaker.getCircuitBreakerConfig().getWaitDurationInOpenState(), methodName);
        }

        return circuitBreaker;
    }

    private EnhanceCircuitBreaker getBackendMonitoredAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (logger.isDebugEnabled()) {
            logger.debug("circuitBreaker parameter is null");
        }
        EnhanceCircuitBreaker circuitBreaker = null;
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        if (targetClass.isAnnotationPresent(EnhanceCircuitBreaker.class)) {
            circuitBreaker = targetClass.getAnnotation(EnhanceCircuitBreaker.class);
            if (circuitBreaker == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("TargetClass has no annotation 'EnhanceCircuitBreaker'");
                }
                circuitBreaker = targetClass.getDeclaredAnnotation(EnhanceCircuitBreaker.class);
                if (circuitBreaker == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("TargetClass has no declared annotation 'EnhanceCircuitBreaker'");
                    }
                }
            }
        }
        return circuitBreaker;
    }

    private Object handleJoinPoint(ProceedingJoinPoint proceedingJoinPoint, io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker, String methodName, RecoveryFunction<?> recoveryFunction) throws Throwable {
        CircuitBreakerUtils.isCallPermitted(circuitBreaker);
        long start = System.nanoTime();
        try {
            Object returnValue = proceedingJoinPoint.proceed();

            long durationInNanos = System.nanoTime() - start;
            circuitBreaker.onSuccess(durationInNanos);
            return returnValue;
        } catch (Throwable throwable) {
            long durationInNanos = System.nanoTime() - start;
            circuitBreaker.onError(durationInNanos, throwable);
            if (logger.isDebugEnabled()) {
                logger.debug("Invocation of method '" + methodName + "' failed!", throwable);
            }
//            throw throwable;
            return recoveryFunction.apply(throwable);
        }
    }

    @Override
    public int getOrder() {
        return circuitBreakerProperties.getCircuitBreakerAspectOrder();
    }
}
