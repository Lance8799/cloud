package org.lance.cloud.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 排除指定请求路径的抽象过滤器
 */
public abstract class ExclusionsFilter implements Filter {

    private String[] ignores;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusions = filterConfig.getInitParameter("exclusions");
        ignores = exclusions.split(",");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isIgnore(httpRequest.getServletPath())){
            chain.doFilter(request, response);
        } else {
            doFilterInternal(httpRequest, httpResponse, chain);
        }
    }

    @Override
    public void destroy() {

    }

    // TODO 忽略特定请求路径，需要优化
    private boolean isIgnore(String path){
        for (String ignore : ignores){
            if (path.contains(ignore))
                return true;
        }
        return false;
    }

    protected abstract void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException;
}
