package org.lance.cloud.txlcn.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lance
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/decrease")
    public String decrease(String userId, Integer money){
        Account account = new Account();
        account.setUserId(userId);
        account.setMoney(money);
        service.decrease(account);
        return "OK";
    }

}
