package org.lance.cloud.sample.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.ApiOperation;
import org.lance.cloud.api.client.SubstituteClient;
import org.lance.cloud.api.result.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/home")
@RefreshScope
public class HomeController {

    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String name;
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private SubstituteClient productClient;


    @GetMapping
    @ApiOperation("测试接口")
    public String test(){
        return "Sample port: " + port;
    }

    @GetMapping("/exception")
    @ApiOperation("异常接口")
    public String exception(){
        return 1/0 + "";
    }

    @GetMapping("/eureka")
    @ApiOperation("Eureka信息接口")
    public Object eureka(){
        return discoveryClient.getInstances(name);
    }

    /**
     * LoadBalanced注解的restTemplate可直接访问服务名
     * @return
     */
    @GetMapping("/sub/test")
    @ApiOperation("Product测试接口")
    public String productTest(){
        return restTemplate.getForObject("http://substitute-service/sub/test", String.class);
    }

    /**
     * 开启了feignClient hystrix会优先使用feign fallback
     * @return
     */
    @GetMapping("/feign/sub/test")
    @ApiOperation("Feign substitute测试接口")
    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public HttpResult feignProductTest(){
        return productClient.test();
    }

    /**
     * feignClient hystrix
     * @return
     */
    @GetMapping("/feign/sub/product")
    @ApiOperation("Feign substitute 产品信息接口")
    public HttpResult product(){
        return productClient.product();
    }

    private String fallback(){
        return "Local fallback";
    }

    /**
     * nacos 配置文件刷新
     * @return
     */
    @GetMapping("/nacos/config")
    @ApiOperation("Nacos配置动态刷新")
    public boolean get() {
        return useLocalCache;
    }

}
