package org.lance.cloud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lance.cloud.domain.transmit.DataTransmit;
import org.lance.cloud.fixture.CacheSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis以及缓存的配置
 *
 * 缓存操作异常处理器，必须继承CachingConfigurerSupport
 */
@Configuration
@ComponentScan(basePackageClasses = CacheSupport.class)
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

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

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        CustomRedisCacheManager cacheManager = new CustomRedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(180);
        return cacheManager;
    }

    /**
     * 自定义的key生成器
     * @return
     */
    @Bean
    public KeyGenerator defaultKeyGenerator(){
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            // 提取到公共类后，导致类名相同
            sb.append(target.getClass().getName());

            if (params.length == 0)
                return sb.toString();

            Object param = params[0];
            if (param instanceof String)
                sb.append(".").append(param);
            else if (param instanceof DataTransmit)
                sb.append(".").append(((DataTransmit) param).cacheId());
            else
                sb.append(".").append(param.toString());

            return sb.toString();
        };
    }

    /**
     * 自定义的redis缓存管理
     * 可通过配置设置缓存名${}
     * 在注解中加上具体的缓存时间，不设置则用全局的
     * 在name后添加#设置时间，单位为秒
     */
    class CustomRedisCacheManager extends RedisCacheManager{

        @Autowired
        DefaultListableBeanFactory beanFactory;

        CustomRedisCacheManager(RedisOperations redisOperations) {
            super(redisOperations);
        }

        @Override
        public Cache getCache(String name) {
            String[] split = name.split("#");
            String cacheName = split[0];
            if (cacheName.startsWith("$"))
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

    /**
     * 缓存操作异常处理器，必须继承CachingConfigurerSupport
     * @return
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler(){
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                logger.error("redis 异常: key=[{}]", key, exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                logger.error("redis 异常: key=[{}]", key, exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                logger.error("redis 异常: key=[{}]", key, exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                logger.error("redis 异常: ", exception);
            }
        };
    }

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
