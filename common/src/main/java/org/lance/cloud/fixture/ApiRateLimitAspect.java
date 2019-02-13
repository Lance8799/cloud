package org.lance.cloud.fixture;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lance.cloud.annotation.RateLimitApi;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * 接口限流切面
 */
@Aspect
@Component
public class ApiRateLimitAspect {

    public static final ConcurrentMap<String, RateLimiter> limiterMap = Maps.newConcurrentMap();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(org.lance.cloud.annotation.RateLimitApi)")
    public Object limit(ProceedingJoinPoint point){
        Object proceed;
        try {
            // 获取方法上限流注解信息
            String methodName = point.getSignature().getName();
            Method method = point.getTarget().getClass().getDeclaredMethod(methodName);
            RateLimitApi limit = method.getAnnotation(RateLimitApi.class);
            String key = limit.key();
            int pps = limit.permitsPerSecond();
            // 设置限流
            limiterMap.putIfAbsent(key, RateLimiter.create(pps));
            if (! limiterMap.get(key).tryAcquire()) {
                return HttpResultBuilder.fail(500, "接口限流");
            }
            proceed = point.proceed();
        } catch (Throwable t) {
            logger.error("接口限流执行异常", t);
            throw new ApplicationException(t);
        }
        return proceed;
    }
}
