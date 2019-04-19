package org.lance.cloud.seata.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lance
 */
@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    public void decrease(Storage storage) {
        Storage entity = repository.findByCommodityCode(storage.getCommodityCode());

        entity.setCount(entity.getCount() - storage.getCount());
        repository.save(entity);
    }
}
