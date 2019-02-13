package org.lance.cloud.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求用时过滤器
 */
public class TimingFilter extends ExclusionsFilter {

    private final Logger logger = LoggerFactory.getLogger(TimingFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);

        logger.info("请求URI：" + request.getRequestURI() + " | 总用时：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
