package org.lance.cloud.resilience.service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.resilience.client.SubstituteClient;
import org.lance.cloud.resilience.fixtrue.EnhanceCircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@CircuitBreaker(name = "annotation")
@EnhanceCircuitBreaker(name = "enhance")
@RateLimiter(name = "enhance")
public class ResilienceAnnotationService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    SubstituteClient client;

    public HttpResult test(){
        return client.test();
    }

    public HttpResult product(){
        return client.product();
    }
}
