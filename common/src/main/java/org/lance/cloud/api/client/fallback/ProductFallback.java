package org.lance.cloud.api.client.fallback;

import org.lance.cloud.api.client.ProductClient;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.transmit.ProductTransmit;
import org.springframework.stereotype.Component;

@Component
public class ProductFallback implements ProductClient {

    @Override
    public String test() {
        return "Fallback test";
    }

    @Override
    public HttpResult<ProductTransmit> product() {
        return new HttpResult<>().ok(new ProductTransmit());
    }
}