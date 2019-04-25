package org.lance.cloud.seata.common.api;

import com.alibaba.fescar.core.context.RootContext;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lance
 */
@FeignClient(name = "seata-order-service", url = "localhost:9092", configuration = OrderClient.ClientConfig.class)
public interface OrderClient {

    /**
     * 创建订单
     * @param userId
     * @param commodityCode
     * @param count
     * @return
     */
    @GetMapping("/order/create")
    String create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count);

    class ClientConfig{
        @Bean
        public Logger.Level feignLoggerLevel(){
            return Logger.Level.FULL;
        }

        @Bean
        public RequestInterceptor seataFeignRequestInterceptor(){
            return (template) -> template.header(RootContext.KEY_XID, RootContext.getXID());
        }
    }
}
