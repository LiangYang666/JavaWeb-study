package com.liang.filter;

import javax.servlet.*;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html;charset=UTF-8");
        System.out.println("执行前");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("执行后");
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");

    }

    public void destroy() {
        System.out.println("销毁");
    }
}
