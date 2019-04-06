package org.lance.cloud.fescar.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.lance.cloud.api.client")
@MapperScan("org.lance.cloud.fescar.account.mapper")
public class FescarAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(FescarAccountApplication.class, args);
    }



}
