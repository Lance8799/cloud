package org.lance.cloud.redis;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.lance.cloud.annotation.EnableRedisCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Redis 相关
 *
 * @author Lance
 */
@SpringBootApplication
@EnableRedisCaching

@EnableMethodCache(basePackages = "org.lance.cloud.redis")
@EnableCreateCacheAnnotation
public class RedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
