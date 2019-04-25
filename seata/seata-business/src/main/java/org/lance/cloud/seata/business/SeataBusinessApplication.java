package org.lance.cloud.seata.business;

import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.lance.cloud.seata.common.fixtrue.EnableSeata;
import org.lance.cloud.seata.common.fixtrue.SeataFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
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
