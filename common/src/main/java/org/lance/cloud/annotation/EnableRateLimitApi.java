package org.lance.cloud.annotation;

import org.lance.cloud.fixture.ApiRateLimitAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用接口限流
 *
 * @see ApiRateLimitAspect
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({ApiRateLimitAspect.class})
public @interface EnableRateLimitApi {
}
