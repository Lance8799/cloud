package org.lance.cloud.seata.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lance
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService service;

    @GetMapping("/decrease")
    public String decrease(String commodityCode, Integer count){
        Storage storage = new Storage();
        storage.setCommodityCode(commodityCode);
        storage.setCount(count);

        service.decrease(storage);
        return "OK";
    }
}
