package org.lance.cloud.config;

import com.google.common.util.concurrent.RateLimiter;
import org.lance.cloud.annotation.RateLimitApi;
import org.lance.cloud.fixture.ApiRateLimitAspect;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@Deprecated
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class ApiRateLimitConfig implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        init();
    }

    private void init(){
        context.getBeansWithAnnotation(RestController.class).forEach((k, v) -> {
            Method[] methods = v.getClass().getDeclaredMethods();
            for (Method method : methods){
                // FIXME 无法获取方法上的注解
                if (method.isAnnotationPresent(RateLimitApi.class)){
                    RateLimitApi limitApi = method.getAnnotation(RateLimitApi.class);
                    ApiRateLimitAspect.limiterMap.putIfAbsent(limitApi.key(), RateLimiter.create(limitApi.permitsPerSecond()));
                }
            }
        });
    }
}
