package org.lance.cloud.txlcn.business;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.lance.cloud.txlcn.common.api.OrderClient;
import org.lance.cloud.txlcn.common.api.StorageClient;
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

    @LcnTransaction
    public void purchase(String userId, String commodityCode, Integer count){
        storageClient.decrease(commodityCode, count);

        orderClient.create(userId, commodityCode, count);
    }
}
