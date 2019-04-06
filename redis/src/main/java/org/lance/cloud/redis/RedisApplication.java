package org.lance.cloud.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Redis 与 Redisson
 */
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    /**
     * 单机模式
     * @return
     */
    @Bean
    RedissonClient redissonSingle() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setTimeout(5000)
                .setConnectionPoolSize(50)
                .setConnectionMinimumIdleSize(10);

        return Redisson.create(config);
    }

    /**
     * 哨兵模式
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.master-name")
    RedissonClient redissonSentinel() {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("10.47.91.83:26379,10.47.91.83:26380")
                .setMasterName("primary")
                .setTimeout(3000)
                .setMasterConnectionPoolSize(50)
                .setSlaveConnectionPoolSize(30);

        return Redisson.create(config);
    }

    /**
     * Spring cache注解支持
     * @param redissonSentinel
     * @return
     */
    RedissonSpringCacheManager redissonSpringCacheManager(RedissonClient redissonSentinel){
        return new RedissonSpringCacheManager(redissonSentinel, "classpath:/cache.yml");
    }

}
