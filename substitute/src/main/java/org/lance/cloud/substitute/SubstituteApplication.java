package org.lance.cloud.substitute;

import org.lance.cloud.annotation.EnableFilters;
import org.lance.cloud.annotation.EnableRateLimitApi;
import org.lance.cloud.annotation.EnableRedisCaching;
import org.lance.cloud.fixture.ApplicationContextHolder;
import org.lance.cloud.fixture.PortCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix

@EnableFilters
@EnableRateLimitApi
@EnableRedisCaching
@Import(ApplicationContextHolder.class)
public class SubstituteApplication {

    public static void main(String[] args) {
        PortCommand.range(10200, 10300).run(args);
        SpringApplication.run(SubstituteApplication.class, args);
    }
}
