package org.lance.cloud.fescar.account.controller;

import lombok.extern.slf4j.Slf4j;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.entity.Account;
import org.lance.cloud.fescar.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService service;

    @PostMapping("/decrease")
    public HttpResult decrease(@RequestBody Account account){
        log.info("请求扣除余额服务：{}", account);
        return service.decrease(account);
    }
}
