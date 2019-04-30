package org.lance.cloud.txlcn.manager;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * txlcn 事务管理端
 * @author Lance
 */
@SpringBootApplication
@EnableTransactionManagerServer
public class TxlcnManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxlcnManagerApplication.class, args);
    }
}
