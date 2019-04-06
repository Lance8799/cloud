package org.lance.cloud.fescar.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.lance.cloud.domain.entity.Account;

public interface AccountMapper extends BaseMapper<Account> {

    @Update("UPDATE t_account SET amount = amount-#{amount} WHERE user_id = #{userId}")
    int decrease(@Param("userId") String userId, @Param("amount") Double amount);
}
