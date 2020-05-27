package org.lance.cloud.seata.common.api;

import feign.Logger;
import feign.RequestInterceptor;
import io.seata.core.context.RootContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lance
 */
@FeignClient(name = "seata-account-service", url = "localhost:9091", configuration = AccountClient.ClientConfig.class)
public interface AccountClient {

    /**
     * 减少用户余额
     * @param userId
     * @param money
     * @return
     */
    @GetMapping("/account/decrease")
    String decrease(@RequestParam("userId") String userId, @RequestParam("money") Integer money);

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
