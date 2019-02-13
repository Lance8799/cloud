package org.lance.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;

/**
 * 在其他路由处理过程中，出现异常时会执行
 */
@Component
public class SendJsonErrorFilter extends ZuulFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String SEND_JSON_ERROR_FILTER_NONE = "SendJsonErrorFilter.none";

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getThrowable() != null && ctx.getBoolean(SEND_JSON_ERROR_FILTER_NONE, true);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        log.error("网关异常信息: {}", throwable.getCause().getMessage());

        if (!ctx.getResponse().isCommitted()) {
            ctx.set(SEND_JSON_ERROR_FILTER_NONE, false);

            HttpServletResponse response = ctx.getResponse();
            response.setContentType("application/json");
            try (PrintWriter writer = response.getWriter()){
                writer.println(JsonUtils.toJson(HttpResultBuilder.fail(500, throwable.getCause().getMessage())));
            } catch (IOException e) {

            }

        }
        return null;
    }
}
