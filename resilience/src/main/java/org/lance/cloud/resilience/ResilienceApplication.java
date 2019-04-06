package org.lance.cloud.resilience;

import org.lance.cloud.resilience.client.SubstituteClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = SubstituteClient.class)
public class ResilienceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResilienceApplication.class, args);
    }

//    @Bean
//    public HealthIndicator substitute(CircuitBreakerRegistry circuitBreakerRegistry){
//        return new CircuitBreakerHealthIndicator(circuitBreakerRegistry.circuitBreaker("annotation"));
//    }
}
