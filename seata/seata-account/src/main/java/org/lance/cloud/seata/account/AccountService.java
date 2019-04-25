package org.lance.cloud.seata.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Lance
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public void decrease(Account account){
        Account entity = repository.findByUserId(account.getUserId());

        entity.setMoney(entity.getMoney() - account.getMoney());
        repository.save(entity);
    }
}
