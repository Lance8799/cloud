package org.lance.cloud.config;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign Client 配置
 */
@Configuration
public class FeignClientConfig {

    /**
     * 默认Feign会将所有方法包在一个circuit breaker中，增加@Scope("prototype")可为每个client创建一个
     * @return
     */
//    @Bean
//    @Scope("prototype")
//    public Feign.Builder feignBuilder(){
//        return Feign.builder();
//    }

    /**
     * NONE: 不输出日志。
     * BASIC: 只输出请求方法的URL和响应的状态码以及接口执行的时间。
     * HEADERS: 将BASIC信息和请求头信息输出。
     * FULL: 输出完整的请求信息
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    /**
     * 添加特定请求头，用于服务间认证拦截
     * @return
     */
    @Bean
    public RequestInterceptor authFeignRequestInterceptor(){
        return (template) -> {
            // 值通过定时任务设置获取
            template.header("Authorization", System.getProperty("auth.token"));
        };
    }

    /**
     * 超时时间，默认连接时间10秒，处理时间60秒
     * @return
     */
    @Bean
    public Request.Options feignRequestOptions(){
        return new Request.Options(5000, 10000);
    }

}
