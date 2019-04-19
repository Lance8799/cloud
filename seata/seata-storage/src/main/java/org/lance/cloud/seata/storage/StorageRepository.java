package org.lance.cloud.seata.storage;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lance
 */
public interface StorageRepository extends JpaRepository<Storage, Integer> {

    Storage findByCommodityCode(String commodityCode);
}
