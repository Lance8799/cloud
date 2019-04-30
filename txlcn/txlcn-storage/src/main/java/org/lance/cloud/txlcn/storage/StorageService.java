package org.lance.cloud.txlcn.storage;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lance
 */
@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    public void decrease(Storage storage) {
        Storage entity = repository.findByCommodityCode(storage.getCommodityCode());

        entity.setCount(entity.getCount() - storage.getCount());
        repository.save(entity);
    }
}
