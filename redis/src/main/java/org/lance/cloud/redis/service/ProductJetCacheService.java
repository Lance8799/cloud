package org.lance.cloud.redis.service;

import com.alicp.jetcache.*;
import com.alicp.jetcache.anno.*;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import com.alicp.jetcache.redis.RedisCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.JavaValueDecoder;
import com.alicp.jetcache.support.JavaValueEncoder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lance.cloud.domain.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Alibaba JetCache 使用
 *
 * @see <a href="https://github.com/alibaba/jetcache/wiki">官网文档</a>
 *
 * @author Lance
 */
@Service
public class ProductJetCacheService {

    private static final Logger logger = LoggerFactory.getLogger(ProductJetCacheService.class);

    @Value("${server.port}")
    private String port;

    /**
     * 注解创建Cache Api
     *
     * @CreateCache
     * cacheType，如果定义为BOTH，会使用LOCAL和REMOTE组合成两级缓存
     * localLimit，如果cacheType为CacheType.LOCAL或CacheType.BOTH，这个参数指定本地缓存的最大元素数量，以控制内存占用
     * localExpire，仅当cacheType为BOTH时适用，为内存中的Cache指定一个不一样的超时时间，通常应该小于expire
     *
     * @CacheRefresh
     * refresh，刷新间隔
     * stopRefreshAfterLastAccess，指定该key多长时间没有访问就停止刷新
     * refreshLockTimeout，类型为BOTH/REMOTE的缓存刷新时，同时只会有一台服务器在刷新，这台服务器会在远程缓存放置一个分布式锁，此配置指定该锁的超时时间
     *
     * @CachePenetrationProtect
     * 当缓存访问未命中的情况下，对并发进行的加载行为进行保护。
     * 当前版本实现的是单JVM内的保护，即同一个JVM中同一个key只有一个线程去加载，其它线程等待结果。
     *
     *
     */
    @CreateCache(name = "productCache:", expire = 60, cacheType = CacheType.BOTH, localLimit = 50)
    @CacheRefresh(timeUnit = TimeUnit.MINUTES, refresh = 60, stopRefreshAfterLastAccess = 100)
    @CachePenetrationProtect
    private Cache<String, Product> cacheAPI;

    /**
     * LoadingCache的get和getAll方法，在缓存未命中的情况下，会调用loader
     */
    @PostConstruct
    public void init(){
        cacheAPI.config().setLoader(key -> Product.generate());
    }

    /**
     * 创建本地缓存
     * <p>对于本地缓存，如果使用jetcache-anno中的@Cached、@CreateCache等注解，必须指定keyConvertor</p>
     * @return
     */
    public Cache<String, Product> createLocalCacheByCaffeine() {
        return CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .loader(key -> Product.generate())
                .buildCache();
    }
    public Cache<String, Product> createLocalCacheByLinkedHashMap() {
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
    public Cache<String, Product> createCache(){
        GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
        pc.setMinIdle(2);
        pc.setMaxIdle(10);
        pc.setMaxTotal(10);
        JedisPool pool = new JedisPool(pc, "localhost", 6379);

        return RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(JavaValueEncoder.INSTANCE)
                .valueDecoder(JavaValueDecoder.INSTANCE)
                .jedisPool(pool)
                .keyPrefix("productCache:")
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .buildCache();
    }


    /**
     * 使用api方式获取缓存
     * @param name
     * @return
     */
    public Product findByJetCacheAPI(String name) {
        return cacheAPI.computeIfAbsent(name, (key) -> Product.generate());
    }

    /**
     * 使用api方式处理异常
     *
     * @param name
     */
    public void doAsyncJetCacheAPI(String name) {
        CacheGetResult<Product> result = cacheAPI.GET(name);
        result.future().thenRun(() -> {
            if (result.isSuccess()) {
                logger.info("Do something... " + result.getValue());
            } else if (result.getResultCode() == CacheResultCode.NOT_EXISTS) {
                logger.info("Cache not exists...");
            } else if (result.getResultCode() == CacheResultCode.EXPIRED) {
                logger.info("Cache expired...");
            }
        });
    }

    /**
     * 分布式锁
     *
     * @param name
     */
    public void doJetCacheDistributedLock(String name) {
        logger.info("[{}] Do lock...", port);

        cacheAPI.tryLockAndRun(name, 30, TimeUnit.SECONDS, () -> {
            logger.info("[{}] Start distributed lock...", port);
            try {
                logger.info("[{}] Do something...", port);
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("[{}] Complete lock...", port);
    }

    /**
     * expire过期时间，单位为秒。 JetCache自动使用所有参数为key
     * 缓存对象需要实现Serializable接口
     * cacheNullValue，当方法返回值为null的时候是否要缓存
     *
     * @param name
     * @return
     */
    @Cached(name = "productCache:", key = "#name", expire = 30, cacheType = CacheType.REMOTE, cacheNullValue = true)
    @CacheRefresh(refresh = 20, stopRefreshAfterLastAccess = 30)
    @CachePenetrationProtect
    public Product findByJetCache(String name) {
        return Product.generate().setName(name);
    }

    /**
     * 更新缓存
     * 使用 SpEL
     *
     * @param product
     */
    @CacheUpdate(name = "productCache:", key = "#product.name", value = "#product")
    public void updateByJetCache(Product product) {
        logger.info("Updated:" + product);
    }
}
