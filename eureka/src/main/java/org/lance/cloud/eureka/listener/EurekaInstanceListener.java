package org.lance.cloud.eureka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Eureka实例监听，可进行一些通知
 */
@Component
public class EurekaInstanceListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event){
        logger.warn(event.getServerId() + "-" + event.getAppName() + " 服务下线" );
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event){
        logger.warn(event.getInstanceInfo().getAppName() + " 服务状态变更" );
    }

//    @EventListener
//    public void listen(EurekaInstanceRenewedEvent event){
//        logger.warn(event.getServerId() + "-" + event.getAppName() + " 服务心跳" );
//    }

}
