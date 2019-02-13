package org.lance.cloud.zuul.controller;

import org.apache.commons.lang.StringUtils;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ErrorHandleController implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public HttpResult error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request);
        String message = (String) errorAttributes.get("message");
        String trace = (String) errorAttributes.get("trace");
        if (StringUtils.isNotBlank(trace)) {
            message += String.format(" and trace %S", trace);
        }
        logger.error("网关过滤器异常：{}", message);
        return HttpResultBuilder.fail(500, message);
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, true);
    }

}
