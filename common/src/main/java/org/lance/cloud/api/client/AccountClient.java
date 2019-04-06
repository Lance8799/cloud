package org.lance.cloud.api.client;

import com.alibaba.fescar.core.context.RootContext;
import feign.Logger;
import feign.RequestInterceptor;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.entity.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "fescar-account-service", path = "/account", configuration = AccountClient.ClientConfig.class)
public interface AccountClient {

    @PostMapping("/decrease")
    HttpResult decrease(Account account);

    class ClientConfig{
        @Bean
        public Logger.Level feignLoggerLevel(){
            return Logger.Level.FULL;
        }

        @Bean
        public RequestInterceptor fescarFeignRequestInterceptor(){
            return (template) -> template.header(RootContext.KEY_XID, RootContext.getXID());
        }
    }
}