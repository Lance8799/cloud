package org.lance.cloud.txlcn.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lance
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService service;

    @GetMapping("/purchase/commit")
    public String purchaseCommit(){
        service.purchase("1002", "2001", 1);
        return "commit";
    }

    @GetMapping("/purchase/rollback")
    public String purchaseRollback(){
        service.purchase("1001", "2001", 1);
        return "rollback";
    }
}
