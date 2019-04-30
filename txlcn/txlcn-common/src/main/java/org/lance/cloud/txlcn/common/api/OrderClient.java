package org.lance.cloud.txlcn.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lance
 */
@FeignClient(name = "txlcn-order-service")
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

}
