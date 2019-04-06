package org.lance.cloud.fescar.caller.controller;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.domain.transmit.BusinessTransmit;
import org.lance.cloud.fescar.caller.service.CallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caller")
public class CallerController {

    @Autowired
    CallerService service;

    @PostMapping("/business")
    public HttpResult handle(BusinessTransmit transmit, boolean flag){
        return service.handle(transmit, flag);
    }

}
