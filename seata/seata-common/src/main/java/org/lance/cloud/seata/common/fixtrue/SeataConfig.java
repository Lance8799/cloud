package org.lance.cloud.seata.common.fixtrue;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fescar.rm.datasource.DataSourceProxy;
import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Lance
 */
@Configuration
public class SeataConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Primary
    @Bean("dataSource")
    public DataSourceProxy dataSource(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    @Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner(applicationName, "my_test_tx_group");
    }

    @Bean
    public FilterRegistrationBean seataFilerRegistration(){
        FilterRegistrationBean<SeataFilter> registrationBean = new FilterRegistrationBean<>(new SeataFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("seataFilter");
        return registrationBean;
    }
}
