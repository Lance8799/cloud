package org.lance.cloud.fescar.account.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.domain.entity.Account;
import org.lance.cloud.fescar.account.mapper.AccountMapper;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends ServiceImpl<AccountMapper, Account> {

    public HttpResult decrease(Account account){

        int count = baseMapper.decrease(account.getUserId(), account.getAmount());

        if (count > 0)
            return HttpResultBuilder.ok(null,"扣除余额成功");

        return HttpResultBuilder.fail("扣除余额失败");
    }
}
