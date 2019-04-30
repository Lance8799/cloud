package org.lance.cloud.txlcn.account;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Lance
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    public void decrease(Account account){
        Account entity = repository.findByUserId(account.getUserId());

        entity.setMoney(entity.getMoney() - account.getMoney());
        repository.save(entity);
    }
}
