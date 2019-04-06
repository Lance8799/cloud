package org.lance.cloud.flux.test;

import org.junit.Test;
import org.lance.cloud.flux.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

/**
 * webFlux客户端测试
 */
public class FluxClientTest {

    private static final String HOST_URL = "http://localhost:11111";

    @Test
    public void testHello() throws InterruptedException {
        WebClient.create(HOST_URL).get().uri("/hello")
                .retrieve().bodyToMono(String.class)
                .subscribe(System.err::println);
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void testTimes(){
        WebClient.create(HOST_URL).get().uri("/times").accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve().bodyToFlux(String.class)
                .log().take(10).blockLast();
    }

    @Test
    public void testUsers(){
        WebClient.create(HOST_URL).get().uri("/users").accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange() // retrive()可以看做是exchange()方法的“快捷版”
                .flatMapMany(response -> response.bodyToFlux(User.class))
                .doOnNext(System.err::println).blockLast();
    }
}
