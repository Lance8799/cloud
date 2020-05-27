package org.lance.cloud.seata.order;

import org.lance.cloud.seata.common.fixtrue.EnableSeata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Lance
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.lance.cloud.seata.common.api")
@EnableSeata
public class SeataOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataOrderApplication.class, args);
    }
}
