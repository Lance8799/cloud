package org.lance.cloud.substitute.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.lance.cloud.annotation.RateLimitApi;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.lance.cloud.fixture.CacheSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sub")
public class SubstituteController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private CacheSupport cacheSupport;

    @GetMapping("/redis/product/{id}")
    @Cacheable(value = "product", keyGenerator = "defaultKeyGenerator")
    public HttpResult<ProductTransmit> productRedis(@PathVariable String id){

        ProductTransmit transmit = new ProductTransmit();
        transmit.setId(id);
        transmit.setName("Substitute Product");
        transmit.setAmount(100);

        return HttpResultBuilder.ok(transmit);
    }

    @GetMapping("/redis/support/product/{id}")
    public HttpResult<ProductTransmit> productRedisSupport(@PathVariable String id){
        ProductTransmit product = cacheSupport.read(id, i -> {

            ProductTransmit transmit = new ProductTransmit();
            transmit.setId(i);
            transmit.setName("Substitute Product");
            transmit.setAmount(100);

            return transmit;
        });
        return HttpResultBuilder.ok(product);
    }

    /**
     * 限流接口
     * @return
     */
    @RateLimitApi(key = "sub.api", permitsPerSecond = 10)
    @GetMapping("/test")
    public HttpResult test(){
        return HttpResultBuilder.ok(null,"Substitute port: " + port);
    }

    @GetMapping("product")
    public HttpResult<ProductTransmit> product(){

        ProductTransmit transmit = new ProductTransmit();
        transmit.setName("Substitute Product");
        transmit.setAmount(100);

        return HttpResultBuilder.ok(transmit);
    }

    /**
     * 熔断接口
     * @return
     */
    @GetMapping("/hystrix")
    @HystrixCommand(fallbackMethod = "fallback")
    public String hystrix(){
        int i = 1/0;
        return "hystrix";
    }
    private String fallback(){
        return "Substitute Fallback";
    }
}
