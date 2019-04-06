package org.lance.cloud.zuul.filter;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.micrometer.core.instrument.MeterRegistry;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentMap;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

/**
 * 具体服务限流过滤器
 *
 * 内存压力大自动开启
 */
@Component
public class ServiceRateLimitFilter extends ZuulFilter {

    private static final ConcurrentMap<String, RateLimiter> limiterMap = Maps.newConcurrentMap();

    private static final RateLimiter defaultLimiter = RateLimiter.create(100);

    // springboot 2.0没有提供
//    @Autowired
//    private SystemPublicMetrics systemPublicMetrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // order一定要大于5，否则，RequestContext.getCurrentContext()里拿不到serviceId等数据
        return PRE_DECORATION_FILTER_ORDER + 2;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getBoolean("success", true)
                && isMemoryOver();
    }

    /**
     * 分布式场景下使用redis实现，存储两个key，一个用于计时，一个用于计数。
     * 请求每调用一次，计数器增加1，若在计时器时间内计数器未超过阈值，则可以处理任务
     * @return
     */
    @Override
    public Object run() {

        RequestContext context = RequestContext.getCurrentContext();
        String serviceId = (String) context.get(SERVICE_ID_KEY);

        RateLimiter limiter = defaultLimiter;

        if (StringUtils.hasText(serviceId)){
            // TODO 每个服务有不同的速率
            limiter = limiterMap.putIfAbsent(serviceId, RateLimiter.create(10));
            if (limiter == null){
                limiter = limiterMap.get(serviceId);
            }
        }

        if (! limiter.tryAcquire()){
            context.set("success", false);
            context.setSendZuulResponse(false);

            HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
            HttpResult result = HttpResultBuilder.fail(status.value(), status.getReasonPhrase());
            context.getResponse().setContentType("application/json;charset=utf-8");
            context.setResponseBody(JsonUtils.toJson(result));
        }
        return null;
    }

    /**
     * 基于内存压力的限流
     * @return
     */
    private boolean isMemoryOver() {
//        Optional<Metric<?>> memFreeMetric = systemPublicMetrics.metrics().stream().filter(m -> "mem.free".equals(m.getName())).findFirst();
//        // 如果不存在这个指标，稳妥起见，开启流控；如果可用内存小于500,000KB，开启流控
//        return memFreeMetric.map(metric -> metric.getValue().longValue() < 500000L).orElse(true);

        // 单位字节
        double memory = meterRegistry.find("jvm.memory.committed").meter().measure().iterator().next().getValue();
        return memory < 500 * 1000 * 1000;
    }
}
