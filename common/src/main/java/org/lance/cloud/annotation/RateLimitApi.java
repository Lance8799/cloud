package org.lance.cloud.annotation;

import org.lance.cloud.fixture.ApiRateLimitAspect;

import java.lang.annotation.*;

/**
 * 接口限流注解
 *
 * 针对指定接口增加限流操作
 * @see ApiRateLimitAspect
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RateLimitApi {

    /**
     * 限流的key
     * @return
     */
    String key() default "default";

    /**
     * 限流速率
     * @return
     */
    int permitsPerSecond() default 1;

}
