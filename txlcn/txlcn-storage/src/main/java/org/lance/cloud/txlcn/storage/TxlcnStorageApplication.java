package org.lance.cloud.txlcn.storage;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lance
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDistributedTransaction
public class TxlcnStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxlcnStorageApplication.class, args);
    }

}
