package studio.deervision.controller;

import studio.deervision.config.security.SystemKeyAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiRestLoggerInterceptor implements AsyncHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRestLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String systemKey = request.getHeader(SystemKeyAuthenticationFilter.SYSTEM_KEY_HEADER);
        systemKey = systemKey == null ? "[no system key]" : systemKey;
        LOGGER.info("API request from {} / {} on URI {}", request.getRemoteAddr(), systemKey, request.getRequestURI());
        return true;
    }

}
