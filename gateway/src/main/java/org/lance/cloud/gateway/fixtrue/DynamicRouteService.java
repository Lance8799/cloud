package org.lance.cloud.gateway.fixtrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 动态路由变更服务
 */
@Component
public class DynamicRouteService implements ApplicationEventPublisherAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 保存路由定义
     * @param definition
     */
    public void save(RouteDefinition definition){
        logger.info("增加网关路由：{}", definition);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void delete(String id){
        routeDefinitionWriter.delete(Mono.just(id)).subscribe();
    }

    public void update(Mono<RouteDefinition> definition){
        logger.info("更新网关路由定义[{}]", definition);

        // FIXME 前删除后保存，直接调用delete方法在没有定义前会报错
    }
}
