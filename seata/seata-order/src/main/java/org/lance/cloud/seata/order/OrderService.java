package org.lance.cloud.seata.order;

import org.lance.cloud.seata.common.api.AccountClient;
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

    public void create(Order order){
        accountClient.decrease(order.getUserId(), order.getCount() * 5);

        if ("1002".equals(order.getUserId())){
            throw new RuntimeException("分布事务回滚");
        }

        order.setMoney(order.getCount() * 5);
        repository.save(order);
    }
}
