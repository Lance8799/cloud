package org.lance.cloud.seata.account;

import org.lance.cloud.seata.common.fixtrue.EnableSeata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lance
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSeata
public class SeataAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataAccountApplication.class, args);
    }

}
