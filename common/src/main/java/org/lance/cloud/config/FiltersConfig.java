package org.lance.cloud.config;

import org.lance.cloud.filter.AuthorizeFilter;
import org.lance.cloud.filter.TimingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 过滤器配置
 *
 * 可用@WebFilter(urlPatterns = "/*", filterName = "TimingFilter") + @ServletComponentScan的方式注册filter
 */
@Configuration
public class FiltersConfig {

    /**
     * 请求认证过滤器，设置了filter.authorize.enabled为true才启用
     * @return
     */
    @Bean
    @Order(Integer.MAX_VALUE - 1)
    @ConditionalOnProperty(prefix = "filter.authorize", name = "enabled", havingValue = "true")
    public FilterRegistrationBean AuthorizeFilerRegistration(){
        FilterRegistrationBean<AuthorizeFilter> registrationBean = new FilterRegistrationBean<>(new AuthorizeFilter());
        registrationBean.addUrlPatterns("/*");
        // 请求中带有token、swagger、api都不过滤
        registrationBean.addInitParameter("exclusions", "jsonRpc,token,swagger");
        registrationBean.setName("authorizeFilter");
        return registrationBean;
    }

    /**
     * 请求用时过滤器，设置了filter.timing.enabled为true才启用
     * @return
     */
    @Bean
    @Order // 默认为MAX_VALUE，则优先级最后
    @ConditionalOnProperty(prefix = "filter.timing", name = "enabled", havingValue = "true")
    public FilterRegistrationBean timingFilerRegistration(){
        FilterRegistrationBean<TimingFilter> registrationBean = new FilterRegistrationBean<>(new TimingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("exclusions", "swagger");
        registrationBean.setName("TimingFilter");
        return registrationBean;
    }


}
