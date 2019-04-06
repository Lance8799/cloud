package org.lance.cloud.fescar.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.lance.cloud.api.client")
@MapperScan("org.lance.cloud.fescar.order.mapper")
public class FescarOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FescarOrderApplication.class, args);
    }
}
