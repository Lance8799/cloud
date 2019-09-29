package org.lance.cloud.redis.controller;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.entity.Product;
import org.lance.cloud.redis.service.ProductJetCacheService;
import org.lance.cloud.redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lance
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductJetCacheService jetCacheService;

    @GetMapping("/jetCache/{name}")
    public HttpResult<Product> getProductByJetCache(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(jetCacheService.findByJetCache(name));
    }

    @PostMapping("/jetCache/{name}")
    public HttpResult updateProductByJetCache(@PathVariable("name") String name) {
        Product product = Product.generate().setName(name);
        jetCacheService.updateByJetCache(product);
        return HttpResultBuilder.ok();
    }

    @GetMapping("/jetCacheAPI/{name}")
    public HttpResult getProductByJetCacheAPI(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(jetCacheService.findByJetCacheAPI(name));
    }

    @GetMapping("/jetCacheAsync/{name}")
    public HttpResult getProductByJetCacheAsync(@PathVariable("name") String name) {
        jetCacheService.doAsyncJetCacheAPI(name);
        return HttpResultBuilder.ok();
    }

    @PutMapping("/jetCacheLock/{name}")
    public HttpResult doLock(@PathVariable("name") String name) {
        jetCacheService.doJetCacheDistributedLock(name);
        return HttpResultBuilder.ok();
    }

    @GetMapping("/caching/{name}")
    public HttpResult<Product> getProductByCaching(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(productService.findByCaching(name));
    }

    @GetMapping("/template/{name}")
    public HttpResult<Product> getProductByTemplate(@PathVariable("name") String name) {
        return HttpResultBuilder.ok(productService.findByTemplate(name));
    }
}
