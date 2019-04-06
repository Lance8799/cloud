package org.lance.cloud.fescar.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.entity.Order;
import org.lance.cloud.fescar.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping("/create")
    public HttpResult create(@RequestBody Order order){
        log.info("请求创建订单服务：{}", order);
        return service.create(order);
    }
}

