package org.lance.cloud.resilience.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.vavr.control.Try;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.lance.cloud.resilience.client.SubstituteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class ResilienceService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public SubstituteClient substituteClient;

    public HttpResult breaker(){
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("breaker");
        Supplier<HttpResult> monoSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, substituteClient::test);

        return Try.ofSupplier(monoSupplier).recover(throwable -> HttpResultBuilder.fail("Fallback")).get();
    }


    public HttpResult limiter(){
        // Create a custom RateLimiter configuration
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(1000))
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .build();

        // Create a RateLimiter
        RateLimiter rateLimiter = RateLimiter.of("limiter", config);

        Supplier<HttpResult<ProductTransmit>> resultSupplier = RateLimiter.decorateSupplier(rateLimiter, substituteClient::product);

        return Try.ofSupplier(resultSupplier).recover(throwable -> HttpResultBuilder.fail("服务限流")).get();
    }
}
