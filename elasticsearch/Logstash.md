# 使用Logstash同步Mysql数据

## 1.下载logstash

**下载的版本要和你的elasticsearch的版本号一致**

## 2.添加mysql驱动

把下载好的mysql-connector-java-{version}.jar放入指定目录，例如：/logstash-home/mysql。

## 3.添加配置文件

新建logstash-mysql.conf，放入指定目录，例如：/logstash-home/config。

    input {
        jdbc {
          jdbc_connection_string => "jdbc:mysql://localhost:3306/db-name"
          jdbc_user => "root"
          jdbc_password => "root"
          # mysql驱动路径
          jdbc_driver_library => "/logstash/mysql/mysql-connector-java-5.1.47.jar"
          jdbc_driver_class => "com.mysql.jdbc.Driver"
          jdbc_paging_enabled => "true"
          jdbc_page_size => "50000"
          record_last_run => "true"
          # 默认use_column_value为false，这样 :sql_last_value为上一次更新的最后时刻值
          use_column_value => "true"
          # 增量值为ID，use_column_value为true时必须设置
          tracking_column => "id"
          # 代表最后一次数据记录id的值存放的位置，会在目录下创建一个名为"meta"的文件
          last_run_metadata_path => "meta"
          # 增量时需要设置
          clean_run => "false"
          # 执行的sql文件路径
          # statement_filepath => "/logstash/mysql/jdbc.sql"
          # 根据ID增量，可根据其他字段作增量。全量只要去掉where
          statement => "select * from product where id > :sql_last_value"
          # 设置监听间隔  各字段含义（由左至右）分、时、天、月、年，全部为*默认含义为每分钟都更新
          schedule => "* * * * *"
        }
    }

    filter {
        json {
            source => "message"
            remove_field => ["message"]
        }
    }
    
    output {
        elasticsearch {
            # ES的IP地址及端口
            hosts => ["localhost:9200"]
            # 索引名称
            index => "elastic-product"
            # 文档名称
            document_type => "product"
            # 需要关联的数据库中有有一个id字段，对应类型中的id
            document_id => "%{id}"
            # 自定义模版时，重写模版
            #template_overwrite => true
            # 自定义模版时，模版路径
            #template => "/logstash/template/logstash-ik.json"
        }
        stdout {
            # JSON格式输出
            codec => json_lines
        }
    }
    
## 4.启动logstash

    .bin/logstash -f config/logstash-mysql.conf