package org.lance.cloud.fescar.caller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.lance.cloud.api.client")
//@EnableErrorHandle
public class FescarCallerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FescarCallerApplication.class, args);
    }
}
