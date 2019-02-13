package org.lance.cloud.api.client;

import org.lance.cloud.api.ProductApi;
import org.lance.cloud.api.client.fallback.ProductFallback;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.config.FeignClientConfig;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * feignClient配置也可以通过配置文件定义feign.client.config.default.*，针对不同将default改为feignName
 * feign hystrix不能提供更多的配置，接口内的fallback类需要ComponentScan增加org.lance.cloud.api
 */
@FeignClient(name = "substitute", path = "/product",
        configuration = FeignClientConfig.class, fallback = ProductFallback.class)
public interface ProductClient extends ProductApi {

    @GetMapping("/test")
    String test();

    @GetMapping("/")
    HttpResult<ProductTransmit> product();

}