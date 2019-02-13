package org.lance.cloud.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * hystrix集群监控
 * Turbine 会通过在 Eureka 中查找服务的 homePageUrl 加上 hystrix.stream 来获取其他服务的监控数据
 *
 * dashboard主页：
 *  http://localhost:port/hystrix
 * 单个服务监控：
 *  在对话框中输入指定服务的ip和端口，http://hystrix-app:port/hystrix.stream
 * 默认turbine集群：
 *  http://turbine-hostname:port/turbine.stream
 * 自定义turbine集群：
 *  http://turbine-hostname:port/turbine.stream?cluster=[clusterName]
 */
@SpringBootApplication
@EnableHystrixDashboard // 只能监控单个节点
@EnableTurbine  // 增加集群监控
public class TurbineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurbineApplication.class, args);
    }
}
