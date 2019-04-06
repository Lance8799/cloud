package org.lance.cloud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redisTemplate配置
 *
 * @since springboot 2.0
 * RedisCacheManager配置方式不同，所以SpringCache使用新的配置类
 * @see RedisCachingConfig
 */
@Configuration
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /*
     * springboot 2.0后不再使用此方式
     *
    @Bean
    @Deprecated
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        CustomRedisCacheManager cacheManager = new CustomRedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(180);
        return cacheManager;
    }
    */

    /*
     * springboot 2 之后RedisCacheManager方式不同
     *
     * 自定义的redis缓存管理
     * 可通过配置设置缓存名${}
     * 在注解中加上具体的缓存时间，不设置则用全局的
     * 在name后添加#设置时间，单位为秒
    @Deprecated
    class CustomRedisCacheManager extends RedisCacheManager{

        @Autowired
        DefaultListableBeanFactory beanFactory;

        @Override
        public Cache getCache(String name) {
            String[] split = name.split("#");
            String cacheName = split[0];
            if (cacheName.startsWith("$"))
                // 通过spring解析${}
                cacheName = beanFactory.resolveEmbeddedValue(cacheName);

            long expiration = this.computeExpiration(cacheName);
            if (split.length > 1){
                String time = split[1];
                expiration = Long.valueOf(time);
            }
            // 设置缓存过期时间, 单位秒
            this.setDefaultExpiration(expiration);
            return super.getCache(cacheName);
        }
    }
    */


    /**
     * 自定义redis序列化，使用json
     * @return
     */
    @Bean
    @Deprecated
    @ConditionalOnProperty("deprecated")
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(objectMapper);
        return serializer;
    }


}
