package org.lance.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Redis限流配置
 */
@Configuration
public class RateLimiterConfig {

    /**
     * 根据用户维度限流
     * @return
     */
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getQueryParams().getFirst("user")));
    }

    /**
     * 根据URI限流
     * @return
     */
    @Bean
    KeyResolver uriKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
    }

    /**
     * 根据主机地址限流
     * @return
     */
    @Bean
    KeyResolver hostKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

}
