package org.lance.cloud.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * MQ方式整合Zipkin，从Edgware开始，使用zipkin-autoconfigure-collector-rabbitmq，
 * 直接添加@EnableZipkinServer而不是@EnableZipkinStreamServer
 * 配置zipkin.collector.rabbitmq.* 指定MQ相关信息
 *
 * 应用端添加spring-cloud-starter-zipkin和spring-rabbit
 * 配置zipkin.rabbitmq.queue=*
 */
@SpringBootApplication
//@EnableZipkinStreamServer
@EnableZipkinServer
public class ZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }
}
