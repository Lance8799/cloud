package org.lance.cloud.sentinel;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.lance.cloud.fixture.PortCommand;
import org.lance.cloud.sentinel.util.SentinelUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Alibaba Sentinel 流量控制、熔断、权限应用服务
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "org.lance.cloud.api.client")
@EnableDiscoveryClient
public class SentinelApplication implements CommandLineRunner {

    public static void main(String[] args) {
        PortCommand.range(10300, 10400).run(args);
        SpringApplication.run(SentinelApplication.class, args);
    }

    /**
     * 配置基于sentinel的restTemplate
     * @return
     */
    @Bean
    @SentinelRestTemplate(fallback = "handleFallback", fallbackClass = SentinelUtil.class,
            blockHandler = "handleException", blockHandlerClass = SentinelUtil.class)
    public RestTemplate sentinelRestTemplate(){
        return new RestTemplate();
    }

    /**
     * Spring boot 启动后执行，通过nacos注册规则
     *
     * 限流规则：
     * [
     *   {
     *     "resource": "abc",
     *     "controlBehavior": 0,
     *     "count": 20.0,
     *     "grade": 1,
     *     "limitApp": "default",
     *     "strategy": 0
     *   }
     * ]
     * 熔断规则：
     * [
     *   {
     *     "resource": "abc0",
     *     "count": 20.0,
     *     "grade": 0,
     *     "passCount": 0,
     *     "timeWindow": 10
     *   }
     * ]
     * 系统规则：
     * [
     *   {
     *     "avgRt": 10,
     *     "highestSystemLoad": 5.0,
     *     "maxThread": 10,
     *     "qps": 20.0
     *   }
     * ]
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        String remoteAddress = "localhost";
        String groupId = "sentinel";
        String dataId = "sentinel.flow.rule";

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, dataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));

        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
