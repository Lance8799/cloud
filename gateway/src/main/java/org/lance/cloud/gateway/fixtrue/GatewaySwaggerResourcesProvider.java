package org.lance.cloud.gateway.fixtrue;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.Map;

/**
 * Gateway通过配置或者服务发现注册swagger资源
 *
 * 根据路由定义信息，构建swagger资源，通过自定义webflux控制器暴露端口
 * 但路由的服务swagger暂时不支持分组，否则取不到资源信息
 *
 * 另外，service-id/swagger-resources能取得服务中定义的swagger资源信息
 * [{"name":"product",
 *   "url":"/v2/api-docs?group=product",
 *   "swaggerVersion":"2.0",
 *   "location":"/v2/api-docs?group=product"}]
 */
@Component
public class GatewaySwaggerResourcesProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";

    private final RouteDefinitionLocator routeLocator;

    @Autowired
    public GatewaySwaggerResourcesProvider(RouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = Lists.newArrayList();

        routeLocator.getRouteDefinitions().subscribe(route -> {
            // predicate设置了Path
            route.getPredicates().stream()
                    .filter(predicates -> "path".equalsIgnoreCase(predicates.getName()))
                    .forEach(predicate -> {

                        String location = "";
                        // 目前已知discovery的key为pattern
                        // properties的key为生成名，默认为 "_genkey_" + i，i表示参数个数
                        for (Map.Entry<String, String> entry : predicate.getArgs().entrySet()) {
                            // 只有一个
                            location = entry.getValue().replace("/**", API_URI);
                        }
                        resources.add(swaggerResource(route.getId(), location));
                    });
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource resource = new SwaggerResource();
        resource.setName(name);
        resource.setLocation(location);
        resource.setSwaggerVersion("2.0");
        return resource;
    }
}
