package org.lance.cloud.redis.cache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.*;
import lombok.extern.slf4j.Slf4j;
import org.lance.cloud.domain.entity.Product;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Alibaba JetCache 使用
 *
 * @see <a href="https://github.com/alibaba/jetcache/wiki">官网文档</a>
 *
 * @author Lance
 */
@Slf4j
@Component
public class ProductJetCache {

    /**
     * 注解创建Cache Api
     *
     * @CreateCache
     * cacheType，如果定义为BOTH，会使用LOCAL和REMOTE组合成两级缓存
     * localLimit，如果cacheType为CacheType.LOCAL或CacheType.BOTH，这个参数指定本地缓存的最大元素数量，以控制内存占用
     * localExpire，仅当cacheType为BOTH时适用，为内存中的Cache指定一个不一样的超时时间，通常应该小于expire
     * keyConvertor, 指定KEY的转换方式，用于将复杂的KEY类型转换为缓存实现可以接受的类型，JetCache内置的可选值为KeyConvertor.FASTJSON和KeyConvertor.NONE。NONE表示不转换，FASTJSON通过fastjson将复杂对象KEY转换成String。如果注解上没有定义，则使用全局配置。
     *
     * @CacheRefresh 会产生两个key去维护
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
    @CreateCache(name = "jetCache:", expire = 60, cacheType = CacheType.BOTH, localLimit = 50)
    @CacheRefresh(timeUnit = TimeUnit.MINUTES, refresh = 60, stopRefreshAfterLastAccess = 100)
    @CachePenetrationProtect
    private Cache<String, Product> cacheApi;

    /**
     * 2.6.0 使用@PostConstruct修改cache会出现循环引用
     * LoadingCache的get和getAll方法，在缓存未命中的情况下，会调用loader
     */
//    @PostConstruct
//    public void init(){
//        cacheApi.config().setLoader(key -> Product.generate());
//    }


    /**
     * expire过期时间，单位为秒。 JetCache自动使用所有参数为key
     * 缓存对象需要实现Serializable接口
     * cacheNullValue，当方法返回值为null的时候是否要缓存
     *
     * @param name
     * @return
     */
    @Cached(name = "product:", key = "#name", expire = 30, cacheType = CacheType.REMOTE, cacheNullValue = true)
    @CacheRefresh(refresh = 20, stopRefreshAfterLastAccess = 30)
    @CachePenetrationProtect
    public Product findByName(String name) {
        return Product.generate().setName(name);
    }


    /**
     * 更新缓存
     * 使用 SpEL
     *
     * @param product
     */
    @CacheUpdate(name = "product:", key = "#product.name", value = "#product")
    public void update(Product product) {
        log.info("Product Cache Updated: {}", product);
    }

    /**
     * 删除缓存
     * @param id
     */
    @CacheInvalidate(name="product:", key="#id")
    public void delete(String id){
        log.info("Product Cache Deleted: {}", id);
    }
}
