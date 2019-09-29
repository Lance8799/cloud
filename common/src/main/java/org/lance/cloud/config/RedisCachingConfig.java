package org.lance.cloud.config;

import org.lance.cloud.domain.transmit.DataTransmit;
import org.lance.cloud.fixture.CacheSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redis缓存的配置
 *
 * 缓存操作异常处理器，必须继承CachingConfigurerSupport
 *
 * @author Lance
 * @since springboot 2.0
 *
 */
@EnableCaching
@Import({CacheSupport.class})
public class RedisCachingConfig extends CachingConfigurerSupport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CachingRedisProp cacheRedisProp;

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CachingRedisProp cacheRedisProp(){
        return new CachingRedisProp();
    }

    /**
     * springboot 2.0后的redisCacheManager配置方式
     *
     * 通过配置文件方式定义，指定缓存名和ttl
     * 同时使用@CacheConfig等注解上指定对应的缓存名
     *
     * @param factory
     * @return
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnProperty("properties")
    public CacheManager redisCacheManager(RedisConnectionFactory factory){
        RedisCacheConfiguration config = defaultRedisCacheConfiguration();
        Map<String, RedisCacheConfiguration> configMap = initConfigMap(config);

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    /**
     * 动态的Redis缓存管理
     *
     * 通过直接在@CacheConfig等注解上设置缓存名和ttl方式，同时支持${}格式
     * 例如：${caching.name:support}#100，#号前为缓存名，后为ttl
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager dynamicRedisCacheManager(RedisConnectionFactory factory){
        return new DynamicRedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(factory),
                defaultRedisCacheConfiguration());
    }

    /**
     * 设置默认的Redis缓存配置
     * @return
     */
    private RedisCacheConfiguration defaultRedisCacheConfiguration() {
        RedisSerializationContext.SerializationPair<String> stringSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
        RedisSerializationContext.SerializationPair<Object> jsonSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(stringSerializationPair)
                .serializeValuesWith(jsonSerializationPair)
                .entryTtl(Duration.ofMinutes(3)) // 3分钟
                .disableCachingNullValues();
    }

    /**
     * 通过配置文件设置不同缓存名的ttl
     *
     * @param config
     * @return
     */
    private Map<String, RedisCacheConfiguration> initConfigMap(RedisCacheConfiguration config) {
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        Map<String, Long> names = cacheRedisProp.getRedis();

        if (! names.isEmpty()){
            names.forEach((key, value) -> configMap.put(key, value > 0 ? config.entryTtl(Duration.ofSeconds(value)) : config));
        }
        return configMap;
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
            sb.append(target.getClass().getSimpleName());

            if (params.length == 0) {
                return sb.toString();
            }

            Object param = params[0];
            if (param instanceof String) {
                sb.append(":").append(param);
            }
            else if (param instanceof DataTransmit) {
                sb.append(":").append(((DataTransmit) param).cacheId());
            }
            else {
                sb.append(":").append(param.toString());
            }
            return sb.toString();
        };
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
                logger.error("redis 缓存获取异常: key=[{}]", key, exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                logger.error("redis 缓存保存异常: key=[{}]", key, exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                logger.error("redis 缓存删除异常: key=[{}]", key, exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                logger.error("redis 缓存清除异常: ", exception);
            }
        };
    }

    /**
     * 动态Redis缓存管理
     *
     * 支持直接在@CacheConfig等注解上设置缓存名和ttl方式，同时支持${}格式
     * 例如：${caching.name:support}#100，#号前为缓存名，后为ttl
     *
     * 同时还可加入更多动态特性，如：到期自动刷新ttl
     *
     */
    static class DynamicRedisCacheManager extends RedisCacheManager{

        private RedisCacheWriter redisCacheWriter;
        private RedisCacheConfiguration defaultRedisCacheConfiguration;

        DynamicRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
            super(cacheWriter, defaultCacheConfiguration);

            this.redisCacheWriter = cacheWriter;
            this.defaultRedisCacheConfiguration = defaultCacheConfiguration;
        }

        @Autowired
        DefaultListableBeanFactory beanFactory;

        /**
         * 当super.getCache从Map找不到对应cache实现类时，会调用此方法同时将返回的cache放入Map
         * @param name
         * @return
         */
        @Override
        protected RedisCache getMissingCache(String name) {
            String[] split = name.split("#");
            String cacheName = split[0];
            if (cacheName.startsWith("$")) {
                // 通过spring解析${}
                cacheName = beanFactory.resolveEmbeddedValue(cacheName);
            }
            RedisCacheConfiguration cacheConfiguration = defaultRedisCacheConfiguration;
            if (split.length > 1){
                long expiration = Long.valueOf(split[1]);
                cacheConfiguration = defaultRedisCacheConfiguration.entryTtl(Duration.ofSeconds(expiration));
            }
            return new DynamicRedisCache(cacheName, redisCacheWriter, cacheConfiguration);
        }

        /**
         * 重新定义RedisCache，用以创建
         * 同时到期刷新ttl也在此类中实现
         */
        static class DynamicRedisCache extends RedisCache{

            DynamicRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
                super(name, cacheWriter, cacheConfig);
            }

            /**
             * 到期刷新ttl
             * @param key
             * @return
             */
            @Override
            public ValueWrapper get(Object key) {
                return super.get(key);
            }
        }
    }


    /**
     * 缓存名，过期时间配置
     */
    @ConfigurationProperties("caching")
    public static class CachingRedisProp {

        Map<String, Long> redis;

        public Map<String, Long> getRedis() {
            return redis;
        }

        public void setRedis(Map<String, Long> redis) {
            this.redis = redis;
        }
    }
}
