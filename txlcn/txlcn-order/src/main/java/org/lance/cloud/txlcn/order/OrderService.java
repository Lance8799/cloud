package org.lance.cloud.txlcn.order;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.lance.cloud.txlcn.common.api.AccountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lance
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private AccountClient accountClient;

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    public void create(Order order){
        accountClient.decrease(order.getUserId(), order.getCount() * 5);

        if ("1001".equals(order.getUserId())){
            throw new RuntimeException("分布事务回滚");
        }

        order.setMoney(order.getCount() * 5);
        repository.save(order);
    }
}
