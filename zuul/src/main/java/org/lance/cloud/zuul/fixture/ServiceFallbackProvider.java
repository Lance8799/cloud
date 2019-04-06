package org.lance.cloud.zuul.fixture;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 网关熔断
 */
@Component
public class ServiceFallbackProvider implements FallbackProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 可以指定服务名，*表示所有服务
     * @return
     */
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        logger.error("网关服务熔断，路由[{}], 错误信息[{}]", route, cause.getCause().getMessage());

        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        }
        return response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // springboot 1.5
//    @Override
//    public ClientHttpResponse fallbackResponse(Throwable cause) {
//        logger.error("网关服务熔断：{}", cause.getCause().getMessage());
//
//        if (cause instanceof HystrixTimeoutException) {
//            return response(HttpStatus.GATEWAY_TIMEOUT);
//        }
//        return response(HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                HttpResult result = HttpResultBuilder.fail(status.value(), status.getReasonPhrase());
                return new ByteArrayInputStream(JsonUtils.toJson(result).getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
