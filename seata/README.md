# Seata
Seata例子运行说明

## 设置Fescar服务器

### 1.配置数据库

**MySQL必须使用InnoDB引擎**

配置数据库信息

    spring:
      datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://x.x.x.x:3306/name
        password: xxx
        username: xxx
        
### 2.创建UNDO_LOG表

**UNDO_LOG表是FESCAR AT模式必须**

    -- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
    CREATE TABLE `undo_log` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `branch_id` bigint(20) NOT NULL,
      `xid` varchar(100) NOT NULL,
      `rollback_info` longblob NOT NULL,
      `log_status` int(11) NOT NULL,
      `log_created` datetime NOT NULL,
      `log_modified` datetime NOT NULL,
      `ext` varchar(100) DEFAULT NULL,
      PRIMARY KEY (`id`),
      UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
    
### 3.启动服务

下载[Fescar服务端](https://github.com/alibaba/fescar/releases), 并解压

    sh fescar-server.sh $LISTEN_PORT $PATH_FOR_PERSISTENT_DATA
    e.g.
    sh fescar-server.sh 8091 /home/admin/fescar/data/

## 设置数据库

查看[初始化脚本](https://github.com/Lance8799/cloud/blob/feature/seata/seata/seata-business/src/main/resources/sql/init.sql)。

## 运行服务

各服务使用Eureka作注册中心，需要先启动Eureka服务。而后依次启动各Seata样例。

## 其他

项目的中file.conf用于配置Seata的连接信息；registry.conf用于配置连接信息的获取方法，目前支持file、nacos、eureka、redis、zk。