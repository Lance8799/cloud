package org.lance.cloud.resilience.client;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "substitute-service", path = "/sub")
public interface SubstituteClient {

    @GetMapping("/test")
    HttpResult test();

    @GetMapping("/product")
    HttpResult<ProductTransmit> product();
}
