package org.lance.cloud.gateway.fixtrue;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.lance.cloud.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

/**
 * 动态路由监听
 */
@Configuration
public class DynamicNacosConfigListener {

    private static final Logger logger = LoggerFactory.getLogger(DynamicNacosConfigListener.class);

    private static final String DYNAMIC_ROUTE_DATA_ID = "dynamic-route";

    @Autowired
    private DynamicRouteService routeService;

    @PostConstruct
    public void init() throws NacosException {
        ConfigService configService = NacosFactory.createConfigService("127.0.0.1:8848");

//         localhost:8848/nacos/v1/cs/configs?dataId=dynamic-route&group=DEFAULT_GROUP
        String config = configService.getConfig(DYNAMIC_ROUTE_DATA_ID, DEFAULT_GROUP, 2000);

        configService.addListener(DYNAMIC_ROUTE_DATA_ID, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String config) {
                logger.info("网关配置更新 : {}", config);

                RouteDefinition routeDefinition = JsonUtils.toObject(config, RouteDefinition.class);
                routeService.save(routeDefinition);
            }
        });
    }
}
