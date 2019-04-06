package org.lance.cloud.fescar.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lance.cloud.api.client.AccountClient;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.entity.Account;
import org.lance.cloud.domain.entity.Order;
import org.lance.cloud.fescar.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    AccountClient accountClient;

    public HttpResult create(Order order){

        Account account = new Account();
        account.setAmount(order.getAmount());
        account.setUserId(order.getUserId());

        HttpResult result = accountClient.decrease(account);

        if (!result.success())
            return HttpResultBuilder.fail("创建订单-扣除余额失败");

        Order entity = new Order();
        entity.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        entity.setCommodityCode(order.getCommodityCode());
        entity.setUserId(order.getUserId());
        entity.setCount(order.getCount());
        entity.setAmount(order.getAmount());

        try{
            baseMapper.create(entity);
        }catch (Exception e){
            return HttpResultBuilder.fail("创建订单失败");
        }

        return HttpResultBuilder.ok(null, "创建订单成功");
    }
}
