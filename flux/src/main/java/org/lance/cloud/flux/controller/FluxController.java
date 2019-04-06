package org.lance.cloud.flux.controller;

import org.lance.cloud.flux.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/flux")
public class FluxController {

    @GetMapping("/hello")
    public Mono<String> hello(){
        return Mono.just("Hello web flux");
    }

    @GetMapping(value = "/date")
    public Mono<String> date(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Mono.just(dateFormat.format(new Date()));
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public Flux<User> user(){
        return Flux.fromStream(findAll()).delayElements(Duration.ofSeconds(1));
    }

    /**
     * 模拟数据库查询用户
     * @return
     */
    private Stream<User> findAll(){
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            list.add(new User(i, "User_" + 1, 20+i));
        }
        return list.stream();
    }
}
