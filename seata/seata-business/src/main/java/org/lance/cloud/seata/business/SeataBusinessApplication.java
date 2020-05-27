package org.lance.cloud.seata.business;

import org.lance.cloud.seata.common.fixtrue.EnableSeata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Seata示例启动类
 *
 * @author Lance
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.lance.cloud.seata.common.api")
@EnableSeata
public class SeataBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataBusinessApplication.class, args);
    }

}
