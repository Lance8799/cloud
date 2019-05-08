# TX-LCN

TX-LCN 主要有两个模块，Tx-Client(TC) 和Tx-Manager(TM)。TC作为微服务下的依赖，TM是独立的服务。可查看官网[快速开始](http://www.txlcn.org/zh-cn/docs/start.html)。

* **txlcn-manager**: TX-LCN服务器端
* **txlcn-common**：通用依赖
* **txlcn-account**：账户服务
* **txlcn-order**：订单服务
* **txlcn-storage**：库存服务
* **txlcn-business**： 业务服务入口

## 环境准备

1.TM需要依赖的中间件： JRE1.8+, Mysql5.6+, Redis3.2+

2.创建MySQL数据库, 名称为: tx-manager

3.创建数据表

    CREATE TABLE `t_tx_exception`  (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
      `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
      `mod_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
      `transaction_state` tinyint(4) NULL DEFAULT NULL,
      `registrar` tinyint(4) NULL DEFAULT NULL,
      `remark` varchar(4096) NULL DEFAULT  NULL,
      `ex_state` tinyint(4) NULL DEFAULT NULL COMMENT '0 未解决 1已解决',
      `create_time` datetime(0) NULL DEFAULT NULL,
      PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

## TM配置与运行

1.配置信息

    spring.application.name=tx-manager
    server.port=7970
    
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/tx-manager?characterEncoding=UTF-8
    spring.datasource.username=root
    spring.datasource.password=root
    
    mybatis.configuration.map-underscore-to-camel-case=true
    mybatis.configuration.use-generated-keys=true
    
    #tx-lcn.logger.enabled=true
    # TxManager Host Ip
    #tx-lcn.manager.host=127.0.0.1
    # TxClient连接请求端口
    #tx-lcn.manager.port=8070
    # 心跳检测时间(ms)
    #tx-lcn.manager.heart-time=15000
    # 分布式事务执行总时间
    #tx-lcn.manager.dtx-time=30000
    #参数延迟删除时间单位ms
    #tx-lcn.message.netty.attr-delay-time=10000
    #tx-lcn.manager.concurrent-level=128
    # 开启日志
    #tx-lcn.logger.enabled=true
    #logging.level.com.codingapi=debug
    #redis 主机
    #spring.redis.host=127.0.0.1
    #redis 端口
    #spring.redis.port=6379
    #redis 密码
    #spring.redis.password=
    
 详细配置说明见[TM配置](http://www.txlcn.org/zh-cn/docs/setting/manager.html)
   
2.运行TM

* 启动txlcn-manager项目。

* 或者从[历史版本TM](https://bbs.txlcn.org/viewtopic.php?id=44)中下载。运行目录下的**package.txmanager.bat**编译，执行**run.txmanager.bat**启动项目。
 
## 启动与访问

依次启动各txlcn样例。

事务正常访问路径：

    http://localhost:9090/business/purchase/commit
    
事务回滚访问路径：

    http://localhost:9090/business/purchase/rollback