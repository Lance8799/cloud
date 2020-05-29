package org.lance.cloud.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.lance.cloud.domain.entity.Product;
import org.lance.cloud.domain.entity.enums.ProductStatus;
import org.lance.cloud.redis.cache.JetCacheHelper;
import org.lance.cloud.redis.cache.ProductJetCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Lance
 */
@Slf4j
@Service
public class ProductService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductJetCache productJetCache;

    @Autowired
    private JetCacheHelper<Product> productJetCacheHelper;

    /**
     * 使用 Jet Cache 查询
     * @param name
     * @return
     */
    public Product findByJetCache(String name){
        return productJetCache.findByName(name);
    }

    /**
     * 使用 Jet Cache 更新
     * @param product
     */
    public void updateByJetCache(Product product) {
        productJetCache.update(product);
    }

    /**
     * 使用 Jet Cache 删除
     * @param id
     */
    public void deleteByJetCache(String id) {
        productJetCache.delete(id);
    }

    /**
     * 使用 Jet Cache Api 异步处理
     * @param name
     */
    public void doAsync(String name) {
        productJetCacheHelper.doAsync(name, (product) -> {
            product.setStatus(ProductStatus.ON_SELL);
            return product;
        });
    }

    /**
     * 使用 Jet Cache Api 分布式锁
     * @param name
     */
    public void doDistributedLock(String name) {
        productJetCacheHelper.doDistributedLock(name, () -> {
            log.info("Used the name: {}", name);
            return null;
        });
    }

    /**
     * 使用 Jet Cache Api 查询
     * @param name
     * @return
     */
    public Product findByJetCacheApi(String name) {
        return productJetCacheHelper.findIfAbsentByApi(name, Product.class);
    }

    /**
     * 使用 Caching 查询
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
