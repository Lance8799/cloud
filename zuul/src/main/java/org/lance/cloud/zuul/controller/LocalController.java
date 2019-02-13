package org.lance.cloud.zuul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocalController {

    @GetMapping("/local/{arg}")
    public String local(@PathVariable String arg){
        return "Local response: " + arg;
    }


    @GetMapping("/local/article/{content}")
    public String localArticle(@PathVariable String content){
        return "Response content:" + content;
    }
}
