package org.lance.cloud.sentinel.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.cloud.alibaba.sentinel.rest.SentinelClientHttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;


/**
 *  方法的参数跟返回值可见 SentinelProtectInterceptor的handleBlockException
 */
public class SentinelUtil {

    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException exception) {
        return new SentinelClientHttpResponse(); // 默认的返回
    }

    public static ClientHttpResponse handleFallback(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException exception) {
        return new SentinelClientHttpResponse(); // 默认的返回
    }

}
