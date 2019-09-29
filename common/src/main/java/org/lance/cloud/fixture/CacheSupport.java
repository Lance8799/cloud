package org.lance.cloud.fixture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @since 2.0
 * 正常情况下cacheNames只能是普通字符串，不支持配置文件
 * FIXME 使cacheNames支持${}形式，目前只能在配置文件中先定义各缓存名，默认名为support
 *
 *
 * @version 1.5
 * 可通过配置文件设置缓存名${cache.name}
 * 在注解中加上具体的缓存时间，不设置则用全局的
 * 在name后添加#设置时间，单位为秒
 *
 * 用法：
 * cacheSupport.read(id, (i) -> {
*      Order order = orderRepositories.findOne(i);
*      return new OrderTransmit().transfer(order);
 * });
 */
@Component
@CacheConfig(cacheNames = "${caching.name:support}#100", keyGenerator = "defaultKeyGenerator")
public class CacheSupport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Cacheable
    public <V> V read(String id, Function<String, V> function){
        if (logger.isDebugEnabled()){
            logger.debug("读取缓存操作，参数{}", id);
        }
        return function.apply(id);
    }

    @CachePut
    public <V> V put(V v){
        if (logger.isDebugEnabled()) {
            logger.debug("写入缓存操作，参数{}", v);
        }
        return v;
    }

//    @CacheEvict(allEntries = true, beforeInvocation = true) //清除所有缓存
    @CacheEvict
    public <V> void remove(V v){
        if (logger.isDebugEnabled()) {
            logger.debug("删除缓存操作，参数{}", v);
        }
    }

}
