package studio.deervision.config.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

public class SystemKeyAuthenticationFilter implements Filter {

    public static final String SYSTEM_KEY_HEADER = "X-SystemKey";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String systemKey = ((HttpServletRequest) request).getHeader(SYSTEM_KEY_HEADER);
            if (isSystemKeyValid(systemKey)) {
                SystemKeyAuthenticationToken apiToken = new SystemKeyAuthenticationToken(systemKey, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(apiToken);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("Invalid user key: " + systemKey);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isSystemKeyValid(String systemKey) {
        if (!StringUtils.hasLength(systemKey)) {
            return false;
        }

        String[] split = systemKey.split("-");
        if(split.length != 2) {
            return false;
        }

        try {
            BigInteger id = new BigInteger(split[0]);
            BigInteger control = new BigInteger(split[1]);
            BigInteger expectedControl = id.mod(BigInteger.valueOf(993481L)).add(BigInteger.valueOf(17L));
            return control.compareTo(expectedControl) == 0;
        }catch (NumberFormatException e) {
            return false;
        }
    }

}