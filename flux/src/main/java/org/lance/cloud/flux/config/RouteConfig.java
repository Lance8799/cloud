package org.lance.cloud.flux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 函数式的编程风格，实现类似MVC
 *
 * H5客户端在：/Users/Lance/Work/Node/web_flux，执行node server.js，访问web_flux.html
 *
 * 服务器推送几种技术：
 * 短轮询：利用ajax定期向服务器请求，无论数据是否更新立马返回数据，高并发情况下可能会对服务器和带宽造成压力；
 * 长轮询：利用comet不断向服务器发起请求，服务器将请求暂时挂起，直到有新的数据的时候才返回，相对短轮询减少了请求次数；
 * SSE：服务端推送（Server Send Event），在客户端发起一次请求后会保持该连接，服务器端基于该连接持续向客户端发送数据，从HTML5开始加入。
 * Websocket：这是也是一种保持连接的技术，并且是双向的，从HTML5开始加入，并非完全基于HTTP，适合于频繁和较大流量的双向通讯场景。
 */
@Configuration
public class RouteConfig {

    /**
     * 路由设置
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> timeRoute(){
        return route(GET("/times"), this::getTime)
                .andRoute(GET("/other"), request -> ServerResponse.ok().body(Mono.just("other"), String.class));
    }

    /**
     * 利用interval生成每秒一个数据的流
     * @param request
     * @return
     */
    private Mono<ServerResponse> getTime(ServerRequest request){
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*") // 解决资源的跨域权限问题
                .contentType(MediaType.TEXT_EVENT_STREAM) // Content-Type为text/event-stream，即SSE
                .body(Flux.interval(Duration.ofSeconds(1))
                        .map(f -> LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)), String.class);
    }
}

