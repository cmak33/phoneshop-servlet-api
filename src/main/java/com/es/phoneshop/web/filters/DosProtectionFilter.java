package com.es.phoneshop.web.filters;


import com.es.phoneshop.service.security.CustomDosProtectionService;
import com.es.phoneshop.service.security.DosProtectionService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DosProtectionFilter implements Filter {

    private final int TOO_MANY_REQUESTS_STATUS = 429;
    private DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        dosProtectionService = CustomDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ip = servletRequest.getRemoteAddr();
        if (dosProtectionService.isUserAllowed(ip)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(TOO_MANY_REQUESTS_STATUS);
        }
    }
}
