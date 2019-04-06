package org.lance.cloud.api.client;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.config.FeignClientConfig;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * feignClient配置也可以通过配置文件定义feign.client.config.default.*，针对不同将default改为feignName
 * feign hystrix不能提供更多的配置，接口内的fallback类需要ComponentScan增加org.lance.cloud.api
 */
@FeignClient(name = "substitute-service", path = "/sub",
        configuration = SubstituteClient.ClientConfig.class, fallback = SubstituteClient.ProductFallback.class)
public interface SubstituteClient {

    @GetMapping("/test")
    HttpResult test();

    @GetMapping("/product")
    HttpResult<ProductTransmit> product();

    /**
     * 配置
     */
    class ClientConfig extends FeignClientConfig{
        @Bean
        public SubstituteClient.ProductFallback productFallback(){
            return new SubstituteClient.ProductFallback();
        }
    }

    /**
     * 熔断
     */
    class ProductFallback implements SubstituteClient {

        @Override
        public HttpResult test() {
            return HttpResultBuilder.fail("ProductClient Fallback test");
        }

        @Override
        public HttpResult<ProductTransmit> product() {
            return HttpResultBuilder.ok(new ProductTransmit());
        }
    }
}
