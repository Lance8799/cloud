package org.lance.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 认证过滤器
 *
 * 白名单认证、token登录认证、服务降级
 *
 */
@Component
public class AuthorizeFilter extends ZuulFilter {

    // 白名单，用逗号分割
    @Value("${white.apis:default}")
    private String whiteApis;

    // 降级服务，用逗号分割
    @Value("${downgrade.service:default}")
    private String downgradeServices;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getBoolean("success", true);
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();

        // API白名单
        List<String> apis = Arrays.asList(whiteApis.split(","));
        if (apis.contains(context.getRequest().getRequestURI()))
            return null;

        // 身份认证
//        String token = context.getRequest().getHeader("token");
//        HttpResult<String> checkResult = JwtUtils.check(token);
//        if (!checkResult.success()){
//            context.set("success", false);
//            context.setSendZuulResponse(false);
//
//            HttpResult result = HttpResultBuilder.fail(checkResult.getCode(), checkResult.getMessage());
//            context.getResponse().setContentType("application/json;charset=utf-8");
//            context.setResponseBody(JsonUtils.toJson(result));
//            return null;
//        }
//        context.addZuulRequestHeader("authId", checkResult.getData());
        return null;
    }
}
