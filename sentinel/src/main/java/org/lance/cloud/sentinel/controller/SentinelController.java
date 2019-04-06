package org.lance.cloud.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.lance.cloud.api.client.SubstituteClient;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.sentinel.service.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentinel")
public class SentinelController {

    @Autowired
    private SentinelService sentinelService;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private SubstituteClient substituteClient;

    /**
     *
     * @return
     */
    @GetMapping("/sub/test")
    public HttpResult test(){
        return substituteClient.test();
    }

    /**
     *
     * @return
     */
    @GetMapping("/test")
    public String getSentinel(){
        return sentinelService.sentinel();
    }

    /**
     * 目前发现，使用@SentinelResource注解的限流时通过blockHandler指定方法返回
     * 而直接配置url的限流时通过UrlBlockHandler接口实现类返回
     * @return
     */
    @GetMapping("/try")
    @SentinelResource(value = "sentinelTry", fallback = "fallback", blockHandler = "blockExpHandler")
    public String trySentinel(){
        return "Try alibaba sentinel";
    }

    public String blockExpHandler(BlockException ex){
        Throwable cause = ex.getCause();

        if (cause instanceof FlowException){
            return "服务限流";
        } else if (cause instanceof DegradeException){
            return "服务熔断";
        }

        return "未知异常";
    }
}
