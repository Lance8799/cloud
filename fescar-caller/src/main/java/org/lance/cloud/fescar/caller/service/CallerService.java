package org.lance.cloud.fescar.caller.service;

import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import org.lance.cloud.api.client.OrderClient;
import org.lance.cloud.api.client.StorageClient;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.entity.Order;
import org.lance.cloud.domain.entity.Storage;
import org.lance.cloud.domain.transmit.BusinessTransmit;
import org.lance.cloud.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class CallerService {

    @Autowired
    OrderClient orderClient;
    @Autowired
    StorageClient storageClient;

    @GlobalTransactional(timeoutMills = 5000, name = "fescar-business")
    public HttpResult handle(BusinessTransmit business, boolean isRollback){

        Order order = new Order();
        order.setUserId(business.getUserId());
        order.setCommodityCode(business.getCommodityCode());
        order.setAmount(business.getAmount().doubleValue());
        order.setCount(business.getCount());

        HttpResult orderResult = orderClient.create(order);

        Storage storage = new Storage();
        storage.setCommodityCode(business.getCommodityCode());
        storage.setCount(business.getCount());

        HttpResult storageResult = storageClient.decrease(storage);

        if (!storageResult.success() || !orderResult.success()){
            throw new ApplicationException("业务处理失败");
        }

        if (isRollback) {
            throw new RuntimeException("分布式事务回滚！");
        }

        return HttpResultBuilder.ok(null, "业务处理成功");
    }
}
