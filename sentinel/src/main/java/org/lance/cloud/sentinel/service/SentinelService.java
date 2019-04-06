package org.lance.cloud.sentinel.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.alibaba.sentinel.custom.SentinelDataSourceHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SentinelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 使用blockHandlerClass可不在同类中定义异常处理，但指定类中的处理方法必需为静态方法
     * @return
     */
    @SentinelResource(value = "sentinel", blockHandler = "exceptionHandler", fallback = "sentinelFallback")
    public String sentinel(){
        return "Alibaba sentinel";
    }

    public String exceptionHandler(BlockException ex) {
        logger.info("Exception handler: [{}]", ex.getMessage());

        if (ex instanceof FlowException){
            return "服务限流";
        } else if (ex instanceof AuthorityException){
            return "权限错误";
        }

        return "未知异常";
    }


    public String sentinelFallback(){
        return "sentinel fall back";
    }


    @EventListener(classes = ApplicationStartedEvent.class)
    public void buildDataSource(ApplicationStartedEvent event) throws Exception {
    }

}
