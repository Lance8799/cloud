## 1.预备环境准备
请确保是在环境中安装使用:

- 64 bit OS Linux/Unix/Mac，推荐使用Linux系统。
- 64 bit JDK 1.8+；下载.配置。
- Maven 3.2.x+；下载.配置。
- 3个或3个以上Nacos节点才能构成集群。

## 2.下载源码或者安装包
**从 Github 上下载源码方式**


	unzip nacos-source.zip
	cd nacos/
	mvn -Prelease-nacos clean install -U
	cd nacos/distribution/target/nacos-server-0.8.0/nacos/bin

**下载编译后压缩包方式**

	unzip nacos-server-0.8.0.zip 或者 tar -xvf nacos-server-0.8.0.tar.gz
	cd nacos/bin

## 3.配置集群配置文件
在nacos的解压目录nacos/的conf目录下，有配置文件cluster.conf，请每行配置成ip:port。（请配置3个或3个以上节点）

	# ip:port
	200.8.9.16:8848
	200.8.9.17:8848
	200.8.9.18:8848

## 4.配置mysql数据库
生产使用建议至少主备模式，或者采用高可用数据库。

**初始化mysql数据库**

	nacos/conf/nacos-mysql.sql

**修改配置文件**

	nacos/conf/application.properties

	spring.datasource.platform=mysql
	// 集群时设置相应数量
	db.num=1 
	// 集群时设置多个索引地址
	db.url.0=jdbc:mysql://11.162.196.16:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
	db.user=nacos_devtest
	db.password=youdontknow

## 5.启动服务器
**Linux/Unix/Mac**

启动命令(在没有参数模式，是集群模式):

	sh startup.sh

**Window**

	cmd startup.cmd 
或者双击 startup.cmd 文件

## 6.访问管理页


	http://localhost:8848/nacos/

**默认账号密码均为nacos**

## 7.关闭服务器

**Linux/Unix/Mac**

	sh shutdown.sh

**Windows**

	cmd shutdown.cmd

或者双击shutdown.cmd运行文件。
