package org.lance.cloud.redis.cache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.CacheResultCode;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import com.alicp.jetcache.redis.RedisCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.JavaValueDecoder;
import com.alicp.jetcache.support.JavaValueEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lance.cloud.domain.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Lance
 */
@Slf4j
@Component
public class JetCacheHelper<T> {

    @Value("${server.port}")
    private String port;

    private Cache<String, T> cacheApi;

    @PostConstruct
    public void init(){
        cacheApi = createCache();
    }

    /**
     * 创建本地缓存
     * <p>对于本地缓存，如果使用jetcache-anno中的@Cached、@CreateCache等注解，必须指定keyConvertor</p>
     * @return
     */
    public Cache<String, T> createLocalCacheByCaffeine() {
        return CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .loader(key -> Product.generate())
                .buildCache();
    }

    public Cache<String, T> createLocalCacheByLinkedHashMap() {
        return LinkedHashMapCacheBuilder.createLinkedHashMapCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .loader(key -> Product.generate())
                .buildCache();
    }

    /**
     * 通过代码方式创建Cache Api
     *
     * @return
     */
    public Cache<String, T> createCache(){
        GenericObjectPoolConfig<T> pc = new GenericObjectPoolConfig<>();
        pc.setMinIdle(2);
        pc.setMaxIdle(10);
        pc.setMaxTotal(10);
        JedisPool pool = new JedisPool(pc, "localhost", 6379, 3000, null, 1);

        return RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(JavaValueEncoder.INSTANCE)
                .valueDecoder(JavaValueDecoder.INSTANCE)
                .jedisPool(pool)
                .loader(key -> null)
                .keyPrefix("jetCacheApi:")
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .buildCache();
    }

    /**
     * 使用api方式获取缓存
     * @param key
     * @return
     */
    public T findIfAbsentByApi(String key, Class<T> clazz) {
        return cacheApi.computeIfAbsent(key, (k) -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * 使用api方式处理异常
     *
     * @param key
     *  缓存键
     * @param successFunction
     *  成功时执行
     */
    public void doAsync(String key, Function<T, Object> successFunction) {
        CacheGetResult<T> result = cacheApi.GET(key);
        result.future().thenRun(() -> {
            if (result.isSuccess()) {
                log.info("Do something... " + result.getValue());
                successFunction.apply(result.getValue());
            } else if (result.getResultCode() == CacheResultCode.NOT_EXISTS) {
                log.info("Cache not exists...");
            } else if (result.getResultCode() == CacheResultCode.EXPIRED) {
                log.info("Cache expired...");
            }
        });
    }

    /**
     * 分布式锁
     *
     * @param key
     */
    public void doDistributedLock(String key, Supplier<Object> something) {
        log.info("[{}] Do lock...", port);

        cacheApi.tryLockAndRun(key, 30, TimeUnit.SECONDS, () -> {
            log.info("[{}] Start distributed lock...", port);
            try {
                log.info("[{}] Do something...", port);
                TimeUnit.SECONDS.sleep(5);
                something.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("[{}] Complete lock...", port);
    }
}
