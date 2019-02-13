package org.lance.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * 测试用的RestTemplate，遇到异常错误跳过
     * @return
     */
    @Bean
    public RestTemplate restTemplate4Test(){
        RestTemplate template = new RestTemplate();

        // 自定义错误处理，统一不报错，方便测试
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });

        return template;
    }

}
