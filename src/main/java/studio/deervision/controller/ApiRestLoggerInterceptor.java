package studio.deervision.controller;

import studio.deervision.config.security.UserKeyAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiRestLoggerInterceptor implements AsyncHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRestLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userKey = request.getHeader(UserKeyAuthenticationFilter.USER_KEY_HEADER);
        userKey = userKey == null ? "[no user key]" : userKey;
        LOGGER.info("API request from {} / {} on URI {}", request.getRemoteAddr(), userKey, request.getRequestURI());
        return true;
    }

}
