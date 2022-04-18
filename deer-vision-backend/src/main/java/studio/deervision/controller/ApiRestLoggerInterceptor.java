package studio.deervision.controller;

import studio.deervision.config.security.RequestKeyAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiRestLoggerInterceptor implements AsyncHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRestLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestKey = request.getHeader(RequestKeyAuthenticationFilter.REQUEST_KEY_HEADER);
        requestKey = requestKey == null ? "[no request key]" : requestKey;
        LOGGER.info("API request from {} / {} on URI {}", request.getRemoteAddr(), requestKey, request.getRequestURI());
        return true;
    }

}
