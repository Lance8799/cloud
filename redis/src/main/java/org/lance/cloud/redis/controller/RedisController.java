package org.lance.cloud.redis.controller;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.entity.Product;
import org.lance.cloud.domain.entity.enums.ProductStatus;
import org.lance.cloud.redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Redis 操作，分别使用JetCache和SpringCaching
 *
 * @author Lance
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private ProductService productService;

    // ------ JetCache 注解操作 ------

    @GetMapping("/jetCache/{name}")
    public HttpResult<Product> getProductByJetCache(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(productService.findByJetCache(name));
    }

    @PostMapping("/jetCache/{name}")
    public HttpResult<?> updateProductByJetCache(@PathVariable("name") String name) {
        Product product = Product.generate().setName(name).setStatus(ProductStatus.FOR_SELL);
        productService.updateByJetCache(product);
        return HttpResultBuilder.ok();
    }

    @DeleteMapping("/jetCache/{id}")
    public HttpResult<?> deleteProductByJetCache(@PathVariable("id") String id) {
        productService.deleteByJetCache(id);
        return HttpResultBuilder.ok();
    }

    // ------ JetCache API 操作 ------

    @GetMapping("/jetCacheApi/{name}")
    public HttpResult<Product> getProductByJetCacheApi(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(productService.findByJetCacheApi(name));
    }

    @GetMapping("/jetCacheApiAsync/{name}")
    public HttpResult<?> getProductByJetCacheAsync(@PathVariable("name") String name) {
        productService.doAsync(name);
        return HttpResultBuilder.ok();
    }

    @PutMapping("/jetCacheApiLock/{name}")
    public HttpResult<?> doLock(@PathVariable("name") String name) {
        productService.doDistributedLock(name);
        return HttpResultBuilder.ok();
    }

    // ------ Spring Caching 操作 ------

    @GetMapping("/caching/{name}")
    public HttpResult<Product> getProductByCaching(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(productService.findByCaching(name));
    }

    // ------ Redis Template 操作 ------

    @GetMapping("/template/{name}")
    public HttpResult<Product> getProductByTemplate(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(productService.findByTemplate(name));
    }
}
