package org.lance.cloud.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class, args);
    }
}
