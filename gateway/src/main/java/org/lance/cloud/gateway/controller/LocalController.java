package org.lance.cloud.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LocalController {

    @GetMapping("/local/article/{content}")
    public String localArticle(@PathVariable String content){
        return "Response content:" + content;
    }

    @GetMapping("/service/fallback")
    public Mono<String> fallback(){
        return Mono.just("网关本地熔断");
    }
}
