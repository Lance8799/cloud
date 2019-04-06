package org.lance.cloud.config;

import org.lance.cloud.exception.ApplicationException;
import org.lance.cloud.exception.enums.ErrorType;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局控制器异常处理配置
 */
@Configuration
public class ErrorHandleConfig {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public GlobalErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                                      ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        return new GlobalErrorController(errorAttributes, serverProperties, errorViewResolversProvider.getIfAvailable());
    }

    /**
     * 自定义全局异常处理
     */
    class GlobalErrorController extends BasicErrorController {

        private ErrorAttributes errorAttributes;

        GlobalErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
            super(errorAttributes, serverProperties.getError(), errorViewResolvers);
            this.errorAttributes = errorAttributes;
        }

        /*
            默认错误信息格式
            {
              "timestamp": "2018-12-04 17:45:23",
              "status": 500,
              "error": "Internal Server Error",
              "exception": "java.lang.reflect.UndeclaredThrowableException",
              "message": "No message available",
              "path": "/seller/substitute/1"
            }
        */
        @Override
        protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {

            Map<String, Object> attr = new LinkedHashMap<>();

            ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
            Integer status = getAttribute(requestAttributes, "javax.servlet.error.status_code");
            String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");

            // springboot 2.0
            WebRequest webRequest = new ServletWebRequest(request);
            Throwable error = errorAttributes.getError(webRequest);
            // springboot 1.5
//            Throwable error = errorAttributes.getError(requestAttributes);

            ErrorType errorType = ErrorType.SYSTEM;
            if (error instanceof ApplicationException) {
                errorType = ((ApplicationException) error).getErrorType();
            }

            attr.put("code", errorType.getCode());
            attr.put("description", errorType.getDescription());
            attr.put("status", status);
            attr.put("message", error != null ? error.getMessage() : "");
            attr.put("path", path);
            attr.put("isRetry", errorType.isRetry());

            return attr;
        }

        @SuppressWarnings("unchecked")
        private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
            return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        }
    }
}
