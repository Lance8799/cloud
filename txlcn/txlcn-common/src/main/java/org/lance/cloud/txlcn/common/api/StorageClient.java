package org.lance.cloud.txlcn.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lance
 */
@FeignClient(name = "txlcn-storage-service")
public interface StorageClient {

    /**
     * 减少库存
     * @param commodityCode
     * @param count
     * @return
     */
    @GetMapping("/storage/decrease")
    String decrease(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count);

}
