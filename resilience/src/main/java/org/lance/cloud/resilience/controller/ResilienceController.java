package org.lance.cloud.resilience.controller;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.resilience.service.ResilienceAnnotationService;
import org.lance.cloud.resilience.service.ResilienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resilience")
public class ResilienceController {

    @Autowired
    private ResilienceService service;
    @Autowired
    private ResilienceAnnotationService annotationService;

    @GetMapping("/breaker")
    public HttpResult breaker(){
        return service.breaker();
    }

    @GetMapping("/limiter")
    public HttpResult limiter(){
        return service.limiter();
    }

    @GetMapping("/annotation/test")
    public HttpResult test(){
        return annotationService.test();
    }

    @GetMapping("/annotation/product")
    public HttpResult product(){
        return annotationService.product();
    }
}
