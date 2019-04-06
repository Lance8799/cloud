package org.lance.cloud.fescar.storage.controller;

import lombok.extern.slf4j.Slf4j;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.entity.Storage;
import org.lance.cloud.fescar.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StorageController {

    @Autowired
    StorageService service;

    @PostMapping("/storage/decrease")
    public HttpResult decrease(@RequestBody Storage storage){
        log.info("请求扣除库存服务：{}", storage);
        return service.decrease(storage);
    }
}
