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
@FeignClient(name = "seata-storage-service", url = "localhost:9093", configuration = StorageClient.ClientConfig.class)
public interface StorageClient {

    /**
     * 减少库存
     * @param commodityCode
     * @param count
     * @return
     */
    @GetMapping("/storage/decrease")
    String decrease(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count);

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
