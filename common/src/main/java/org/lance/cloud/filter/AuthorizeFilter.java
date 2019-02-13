package org.lance.cloud.filter;

import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.exception.enums.ErrorType;
import org.lance.cloud.utils.JsonUtils;
import org.lance.cloud.utils.JwtUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 服务间请求认证过滤
 *
 */
public class AuthorizeFilter extends ExclusionsFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        // 请求中是否带有指定请求头
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)){
            HttpResult result = HttpResultBuilder.fail(ErrorType.ILLEGAL_REQUEST.getCode(), "缺少认证信息");
            response.getWriter().println(JsonUtils.toJson(result));
            return;
        }
        // 验证token合法性
        HttpResult result = JwtUtils.check(token);
        if (HttpResult.OK != result.getCode()){
            response.getWriter().println(JsonUtils.toJson(result));
            return;
        }
        chain.doFilter(request, response);
    }

}
