package org.lance.cloud.txlcn.business;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Lance
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.lance.cloud.txlcn.common.api")
@EnableDistributedTransaction
public class TxlcnBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxlcnBusinessApplication.class, args);
    }

}
