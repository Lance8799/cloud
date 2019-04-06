package org.lance.cloud.sample;

import org.lance.cloud.annotation.EnableAspect;
import org.lance.cloud.annotation.EnableErrorHandle;
import org.lance.cloud.annotation.EnableFilters;
import org.lance.cloud.fixture.PortCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.lance.cloud.api.client")
@EnableHystrix
//@ComponentScan(basePackages = {"org.lance.cloud.sample", "org.lance.cloud.api.client"})

@EnableErrorHandle
@EnableAspect
@EnableFilters
//@EnableTokenSchedule
public class SampleApplication {

    @Bean
    @LoadBalanced //加上LoadBalanced可以直接通过服务名调用
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        PortCommand.range(10100, 10200).run(args);
        SpringApplication.run(SampleApplication.class, args);
    }
}
