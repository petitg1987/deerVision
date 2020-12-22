package com.urchin.release.mgt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiRestLoggerInterceptor implements AsyncHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRestLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LOGGER.info("API request from {} on URI {}", request.getRemoteAddr(), request.getRequestURI());
        return true;
    }

}
