package org.lance.cloud.txlcn.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lance
 */
@FeignClient(name = "txlcn-account-service")
public interface AccountClient {

    /**
     * 减少用户余额
     * @param userId
     * @param money
     * @return
     */
    @GetMapping("/account/decrease")
    String decrease(@RequestParam("userId") String userId, @RequestParam("money") Integer money);

}
