package org.lance.cloud.api.client;

import org.lance.cloud.api.ProductApi;
import org.lance.cloud.api.request.AuthRequest;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * feignClient配置也可以通过配置文件定义feign.client.config.default.*，针对不同将default改为feignName
 * feign hystrix不能提供更多的配置，接口内的fallback类需要ComponentScan增加org.lance.cloud.api
 */
@FeignClient(name = "zuul", path = "/auth", configuration = FeignClientConfig.class)
public interface AuthorizeClient extends ProductApi {

    @GetMapping("/token")
    HttpResult<String> token(AuthRequest auth);

}