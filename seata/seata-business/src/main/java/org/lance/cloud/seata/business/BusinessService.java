package org.lance.cloud.seata.business;

import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import org.lance.cloud.seata.common.api.OrderClient;
import org.lance.cloud.seata.common.api.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lance
 */
@Service
public class BusinessService {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private StorageClient storageClient;

    @GlobalTransactional(name = "seata-business")
    public void purchase(String userId, String commodityCode, Integer count){
        storageClient.decrease(commodityCode, count);

        orderClient.create(userId, commodityCode, count);
    }
}
