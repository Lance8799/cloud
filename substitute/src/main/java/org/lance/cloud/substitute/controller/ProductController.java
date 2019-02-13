package org.lance.cloud.substitute.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.lance.cloud.annotation.RateLimitApi;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${server.port}")
    private String port;

    @RateLimitApi(key = "product.api", permitsPerSecond = 10)
    @GetMapping("/test")
    public String test(){
        return "Product port: " + port;
    }

    @GetMapping("/")
    public HttpResult<ProductTransmit> product(){
        ProductTransmit transmit = new ProductTransmit();
        transmit.setName("默认产品");
        transmit.setAmount(100);
        return HttpResultBuilder.ok(transmit);
    }

    @GetMapping("/hystrix")
    @HystrixCommand(fallbackMethod = "fallback")
    public String hystrix(){
        int i = 1/0;
        return "hystrix";
    }

    private String fallback(){
        return "substitute fallback";
    }
}
