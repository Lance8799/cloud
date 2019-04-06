package org.lance.cloud.fescar.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.lance.cloud.api.client")
@MapperScan("org.lance.cloud.fescar.storage.mapper")
public class FescarStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FescarStorageApplication.class, args);
    }
}
