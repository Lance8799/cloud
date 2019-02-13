package org.lance.cloud.sample.rule;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

/**
 * 自定义ribbon负载均衡策略，始终返回第一个服务
 */
public class MyRibbonRule implements IRule {

    private ILoadBalancer lb;

    public Server choose(Object key) {
        return lb.getAllServers().get(0);
    }

    public void setLoadBalancer(ILoadBalancer lb) {
        this.lb = lb;
    }

    public ILoadBalancer getLoadBalancer() {
        return lb;
    }
}
