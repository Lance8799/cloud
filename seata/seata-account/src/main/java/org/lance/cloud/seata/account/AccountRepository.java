package org.lance.cloud.seata.account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lance
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByUserId(String userId);
}
