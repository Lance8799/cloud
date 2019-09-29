package org.lance.cloud.redis.service;

import org.lance.cloud.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Lance
 */
@Service
public class ProductService {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 使用redis caching
     *
     * cacheNames 为缓存名，后面跟#为过期时间，单位为秒
     *
     * @param name
     * @return
     */
    @Cacheable(cacheNames = "products#30", keyGenerator = "defaultKeyGenerator")
    public Product findByCaching(String name) {
        return Product.generate().setName(name);
    }

    /**
     * 使用redisTemplate
     *
     * @param name
     * @return
     */
    public Product findByTemplate(String name) {
        String key = "product:find:" + name;

        Product product = (Product) redisTemplate.opsForValue().get(key);
        if (product == null) {
            product = Product.generate().setName(name);
            redisTemplate.opsForValue().set(key, product, 30, TimeUnit.SECONDS);
        }
        return product;
    }

}
