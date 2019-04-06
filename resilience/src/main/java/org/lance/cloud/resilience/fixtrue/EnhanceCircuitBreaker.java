package org.lance.cloud.resilience.fixtrue;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface EnhanceCircuitBreaker {

    String name();

    Class<? extends RecoveryFunction> recovery() default DefaultRecoveryFunction.class ;
}
