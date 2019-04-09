# Cloud

基于Spring Boot 2.x，整合Spring Cloud相关的常用模块，简易的使用例子。

## 模块

* **admin**：监控应用，针对服务提供简明的监控界面。

* **common**：通用的依赖，提供整体的AOP、配置、注解、过滤、API功能。
* **eureka**：Netflix的注册中心。
* **fescar-account**：Fescar账户服务
* **fescar-order**：Fescar订单服务
* **fescar-storage**：Fescar库存服务
* **fescar-caller**： Alibaba Fescar分布式事务框架，服务间调用入口例子。
* **flux**：Spring WebFlux，非阻塞响应式Web框架例子。
* **gateway**：Spring Gateway，zuul网关的替代，基于WebFlux开发。
* **redis**：Spring Redis和Redisson相关。
* **resilience**：Hystrix的替代，提供熔断降级、流量控制等模块。
* **sample**：模块整合例子，整合了RabbitMQ，Zipkin，Feign，Hystrix，Cache，Sentinel，Nacos等。
* **substitute**：模块整合例子，主要提供sample模块调用。
* **sentinel**：Alibaba Sentinel，流量控制、熔断降级、系统负载保护等多个维度保护服务。
* **turbine**：Hystrix集群管理。
* **txlcn-manager**：LCN分式式事务框架服务端。
* **zipkin**：微服务链路跟踪系统，收集和查找各服务间链路，提供简明的管理界面。
* **zuul**：Netflix的网关服务，包括路径规则、服务限流等。

## 依赖

* **部分模块使用到Swagger，依赖于[swagger-spring-boot-starter](https://github.com/Lance8799/swagger-spring-boot-starter)。需要在本地使用mvn install布署到本地maven仓库。**

* 运行Fescar例子需要[Fescar服务端](https://github.com/Lance8799/cloud/blob/2.0/FescarServer.md)和[初始化数据库](https://github.com/Lance8799/cloud/blob/2.0/fescar-caller/src/main/resources/fescar.sql)。

* 注册中心可替换为[Alibaba Nacos](https://github.com/alibaba/Nacos)，需配置Nacos服务。设置可查看[NacosServer.md](https://github.com/Lance8799/cloud/blob/2.0/NacosServer.md)或查看[官网](https://nacos.io/zh-cn/)。
